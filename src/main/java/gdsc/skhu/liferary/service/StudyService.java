package gdsc.skhu.liferary.service;

import gdsc.skhu.liferary.domain.DTO.ImageDTO;
import gdsc.skhu.liferary.domain.DTO.StudyDTO;
import gdsc.skhu.liferary.domain.MainPost;
import gdsc.skhu.liferary.domain.Member;
import gdsc.skhu.liferary.domain.Study;
import gdsc.skhu.liferary.repository.mainpost.MainPostRepository;
import gdsc.skhu.liferary.repository.MemberRepository;
import gdsc.skhu.liferary.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class StudyService {
    private final MainPostRepository mainPostRepository;
    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;
    private final ImageService imageService;
    private final EntityManager entityManager;

    // Create
    public StudyDTO.Response save(String username, StudyDTO.Request request) throws IOException {
        MainPost mainPost = mainPostRepository.findById(request.getMainPostId())
                .orElseThrow(() -> new NoSuchElementException("Main post not found"));
        if(studyRepository.findByMainPost(mainPost).isPresent()) {
            throw new IllegalArgumentException("Study is already exist");
        }
        Study study = Study.builder()
                .mainPost(mainPost)
                .title(request.getTitle())
                .author(memberRepository.findByEmail(username)
                        .orElseThrow(() -> new NoSuchElementException("Member not found")))
                .context(request.getContext())
                .images(new ArrayList<>())
                .build();
        saveWithImage(study, request.getImages());
        return new StudyDTO.Response(study);
    }

    // Read
    @Transactional(readOnly = true)
    public Page<StudyDTO.Response> findAll(Pageable pageable) {
        return studyRepository.findAll(pageable).map(study -> {
            if(study.getImages() != null) {
                study.getImages().replaceAll(
                        storedImageName -> imageService.findByStoredImageName(storedImageName).getImagePath()
                );
            }
            return study;
        }).map(StudyDTO.Response::new);
    }

    @Transactional(readOnly = true)
    public StudyDTO.Response findByMainPost(Long mainPostId) {
        MainPost mainPost = mainPostRepository.findById(mainPostId)
                .orElseThrow(() -> new NoSuchElementException("Main post not found"));
        return new StudyDTO.Response(studyRepository.findByMainPost(mainPost).map(study -> {
            if(study.getImages() != null) {
                study.getImages().replaceAll(
                        storedImageName -> imageService.findByStoredImageName(storedImageName).getImagePath()
                );
            }
            return study;
        }).orElseThrow(() -> new NoSuchElementException("Study not found")));
    }

    @Transactional(readOnly = true)
    public Page<StudyDTO.Response> findByMember(Pageable pageable, String username) {
        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new NoSuchElementException("Member not found"));
        return studyRepository.findByAuthor(pageable, member).map(study -> {
            if(study.getImages() != null) {
                study.getImages().replaceAll(
                        storedImageName -> imageService.findByStoredImageName(storedImageName).getImagePath()
                );
            }
            return study;
        }).map(StudyDTO.Response::new);
    }

    // Update
    @Transactional
    public StudyDTO.Response update(String username, StudyDTO.Update update, Long mainPostId) throws IOException {
        MainPost mainPost = mainPostRepository.findById(mainPostId)
                .orElseThrow(() -> new NoSuchElementException("Main post not found"));
        Study oldStudy = studyRepository.findByMainPost(mainPost)
                .orElseThrow(() -> new NoSuchElementException("Study not found"));
        Study newStudy;
        if(oldStudy.getAuthor().getEmail().equals(username)) {
            newStudy = Study.builder()
                    .id(oldStudy.getId())
                    .mainPost(mainPost)
                    .title(update.getTitle())
                    .author(oldStudy.getAuthor())
                    .context(update.getContext())
                    .images(new ArrayList<>())
                    .build();
        } else {
            throw new AuthorizationServiceException("Unauthorized access");
        }
        saveWithImage(newStudy, update.getImages());
        return new StudyDTO.Response(newStudy);
    }

    // Delete
    @Transactional
    public ResponseEntity<String> delete(Long mainPostId) {
        MainPost mainPost = mainPostRepository.findById(mainPostId)
                .orElseThrow(() -> new NoSuchElementException("Main post not found"));
        Study study = studyRepository.findByMainPost(mainPost)
                .orElseThrow(() -> new NoSuchElementException("Study not found"));
        try {
            if(study.getImages() != null) {
                for(String imageName : study.getImages()) {
                    imageService.deleteImage("path/", imageName);
                }
            }
            studyRepository.delete(study);
        } catch (Exception e) {
            return new ResponseEntity<>("Exception occurred", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("Delete success");
    }

    // Util
    private void saveWithImage(Study study, List<String> images) {
        if(images != null) {
            for (String imagePath : images) {
                ImageDTO.Response image = imageService.findByImagePath(imagePath);
                study.getImages().add(image.getStoredImageName());
            }
        }
        studyRepository.saveAndFlush(study);
        entityManager.detach(study);
        if(study.getImages() != null) {
            study.getImages().replaceAll(storedImageName -> imageService.findByStoredImageName(storedImageName).getImagePath());
        }
    }
}
