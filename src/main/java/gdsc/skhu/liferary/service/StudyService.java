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
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class StudyService {
    private final MainPostRepository mainPostRepository;
    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;

    // Create
    public StudyDTO.Response save(StudyDTO.Request request) {
        Study study = Study.builder()
                .mainPost(mainPostRepository.findById(request.getMainPostId())
                        .orElseThrow(() -> new NoSuchElementException("Main post not found")))
                .title(request.getTitle())
                .author(memberRepository.findByUsername(request.getAuthor()))
                .context(request.getContext())
                .build();
        return new StudyDTO.Response(studyRepository.save(study));
    }

    // Read
    public StudyDTO.Response findByMainPost(Long mainPostId) {
        MainPost mainPost = mainPostRepository.findById(mainPostId)
                .orElseThrow(() -> new NoSuchElementException("Main post not found"));
        return new StudyDTO.Response(studyRepository.findByMainPost(mainPost)
                .orElseThrow(() -> new NoSuchElementException("Study not found")));
    }

    // Update
    public StudyDTO.Response update(StudyDTO.Update update, Long mainPostId) {
        MainPost mainPost = mainPostRepository.findById(mainPostId)
                .orElseThrow(() -> new NoSuchElementException("Main post not found"));
        Study oldStudy = studyRepository.findByMainPost(mainPost)
                .orElseThrow(() -> new NoSuchElementException("Study not found"));
        Study newStudy = Study.builder()
                .id(oldStudy.getId())
                .mainPost(mainPost)
                .title(update.getTitle())
                .author(oldStudy.getAuthor())
                .context(update.getContext())
                .build();
        studyRepository.save(newStudy);
        return this.findByMainPost(mainPostId);
    }

    // Delete
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
