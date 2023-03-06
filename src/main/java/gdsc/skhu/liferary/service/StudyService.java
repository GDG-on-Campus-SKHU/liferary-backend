package gdsc.skhu.liferary.service;

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
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class StudyService {
    private final MainPostRepository mainPostRepository;
    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;

    // Create
    @Transactional
    public StudyDTO.Response save(Principal principal, StudyDTO.Request request) {
        Study study = Study.builder()
                .mainPost(mainPostRepository.findById(request.getMainPostId())
                        .orElseThrow(() -> new NoSuchElementException("Main post not found")))
                .title(request.getTitle())
                .author(memberRepository.findByEmail(principal.getName())
                        .orElseThrow(() -> new NoSuchElementException("Member not found")))
                .context(request.getContext())
                .build();
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
    public StudyDTO.Response update(Principal principal, StudyDTO.Update update, Long mainPostId) {
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
                    .build();
        } else {
            throw new AuthorizationServiceException("Unauthorized access");
        }
        studyRepository.save(newStudy);
        return this.findByMainPost(mainPostId);
    }

    // Delete
    @Transactional
    public ResponseEntity<String> delete(Long mainPostId) {
        MainPost mainPost = mainPostRepository.findById(mainPostId)
                .orElseThrow(() -> new NoSuchElementException("Main post not found"));
        try {
            studyRepository.delete(studyRepository.findByMainPost(mainPost)
                    .orElseThrow(() -> new NoSuchElementException("Study not found")));
        } catch (Exception e) {
            return new ResponseEntity<String>("Exception occurred", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("Delete success");
    }
}
