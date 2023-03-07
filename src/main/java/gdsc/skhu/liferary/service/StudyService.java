package gdsc.skhu.liferary.service;

import gdsc.skhu.liferary.domain.BoardPost;
import gdsc.skhu.liferary.domain.DTO.ImageDTO;
import gdsc.skhu.liferary.domain.DTO.StudyDTO;
import gdsc.skhu.liferary.domain.MainPost;
import gdsc.skhu.liferary.domain.Study;
import gdsc.skhu.liferary.repository.MainPostRepository;
import gdsc.skhu.liferary.repository.MemberRepository;
import gdsc.skhu.liferary.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class StudyService {
    private final MainPostRepository mainPostRepository;
    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;
    private final ImageService imageService;

    // Create
    @Transactional
    public StudyDTO.Response save(Principal principal, StudyDTO.Request request) throws IOException {
        Study study = Study.builder()
                .mainPost(mainPostRepository.findById(request.getMainPostId())
                        .orElseThrow(() -> new NoSuchElementException("Main post not found")))
                .title(request.getTitle())
                .author(memberRepository.findByEmail(principal.getName())
                        .orElseThrow(() -> new NoSuchElementException("Member not found")))
                .context(request.getContext())
                .images(new ArrayList<>())
                .build();
        if(request.getImages() != null) {
            for(MultipartFile file : request.getImages()) {
                ImageDTO.Response image = imageService.uploadImage("board/", file);
                study.getImages().add(image.getImagePath());
            }
        }
        return new StudyDTO.Response(studyRepository.save(study));
    }

    // Read
    @Transactional
    public StudyDTO.Response findByMainPost(Long mainPostId) {
        MainPost mainPost = mainPostRepository.findById(mainPostId)
                .orElseThrow(() -> new NoSuchElementException("Main post not found"));
        return new StudyDTO.Response(studyRepository.findByMainPost(mainPost)
                .orElseThrow(() -> new NoSuchElementException("Study not found")));
    }

    // Update
    @Transactional
    public StudyDTO.Response update(Principal principal, StudyDTO.Update update, Long mainPostId) throws IOException {
        MainPost mainPost = mainPostRepository.findById(mainPostId)
                .orElseThrow(() -> new NoSuchElementException("Main post not found"));
        Study oldStudy = studyRepository.findByMainPost(mainPost)
                .orElseThrow(() -> new NoSuchElementException("Study not found"));
        Study newStudy;
        if(oldStudy.getAuthor().getEmail().equals(principal.getName())) {
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

        if(update.getImages() != null) {
            for(MultipartFile file : update.getImages()) {
                ImageDTO.Response image = imageService.uploadImage("main/", file);
                newStudy.getImages().add(image.getStoredImageName());
            }
        }
        newStudy = studyRepository.save(newStudy);
        if(newStudy.getImages() != null) {
            for(int i = 0; i < newStudy.getImages().size(); i++) {
                newStudy.getImages().set(i, imageService.findByStoredImageName(newStudy.getImages().get(i)).getImagePath());
            }
        }
        return this.findByMainPost(mainPostId);
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
                for(String path : study.getImages()) {
                    imageService.deleteImage(path);
                }
            }
            studyRepository.delete(study);
        } catch (Exception e) {
            return new ResponseEntity<>("Exception occurred", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("Delete success");
    }
}
