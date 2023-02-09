package gdsc.skhu.liferary.service;

import gdsc.skhu.liferary.domain.DTO.MainPostDTO;
import gdsc.skhu.liferary.domain.MainPost;
import gdsc.skhu.liferary.repository.MainPostRepository;
import gdsc.skhu.liferary.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MainPostService {
    private final MemberRepository memberRepository;
    private final MainPostRepository mainPostRepository;

    // Create
    public MainPostDTO.Response save(MainPostDTO.Request request) {
        MainPost mainPost = MainPost.builder()
                .title(request.getTitle())
                .author(memberRepository.findByUsername(request.getAuthor()))
                .category(request.getCategory())
                .context(request.getContext())
                .video(request.getVideo())
                .build();
        return new MainPostDTO.Response(mainPostRepository.save(mainPost));
    }

    // Update
    public MainPostDTO.Response findById(Long id) {
        return new MainPostDTO.Response(mainPostRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("There is no Main Post with this ID")));
    }

    public Page<MainPostDTO.Response> findByCategory(Pageable pageable, String category) {
        return mainPostRepository.findByCategory(pageable, category).map(MainPostDTO.Response::new);
    }

    public Page<MainPostDTO.Response> findAll(Pageable pageable) {
        return mainPostRepository.findAll(pageable).map(MainPostDTO.Response::new);
    }

    // Update
    public MainPostDTO.Response update(MainPostDTO.Update update, Long id) {
        MainPost oldMainPost = mainPostRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("There is no Main Post with this ID"));
        MainPost newMainPost = MainPost.builder()
                .id(id)
                .title(update.getTitle())
                .author(oldMainPost.getAuthor())
                .category(update.getCategory())
                .context(update.getContext())
                .video(update.getVideo())
                .build();
        mainPostRepository.save(newMainPost);
        return this.findById(id);
    }

    // Delete
    public ResponseEntity<String> delete(Long id) {
        try {
            mainPostRepository.deleteById(id);
        } catch (Exception e) {
            return new ResponseEntity<String>("Exception occurred", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("Delete success");
    }
}
