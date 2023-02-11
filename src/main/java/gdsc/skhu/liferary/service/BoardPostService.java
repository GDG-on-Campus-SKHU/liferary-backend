package gdsc.skhu.liferary.service;

import gdsc.skhu.liferary.domain.BoardPost;
import gdsc.skhu.liferary.domain.DTO.BoardPostDTO;
import gdsc.skhu.liferary.domain.MainPost;
import gdsc.skhu.liferary.repository.BoardPostRepository;
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
public class BoardPostService {
    public final MainPostRepository mainPostRepository;
    public final BoardPostRepository boardPostRepository;
    public final MemberRepository memberRepository;

    // Create
    public BoardPostDTO.Response save(BoardPostDTO.Request request) {
        BoardPost boardPost = BoardPost.builder()
                .mainPost(mainPostRepository.findById(request.getMainPostId())
                        .orElseThrow(() -> new NoSuchElementException("Main post not found")))
                .title(request.getTitle())
                .author(memberRepository.findByUsername(request.getAuthor()))
                .context(request.getContext())
                .build();
        return new BoardPostDTO.Response(boardPostRepository.save(boardPost));
    }

    //Read
    public Page<BoardPostDTO.Response> findByMainPost(Pageable pageable, Long mainPostId) {
        MainPost mainPost = mainPostRepository.findById(mainPostId)
                .orElseThrow(() -> new NoSuchElementException("Main post not found"));
        return boardPostRepository.findByMainPost(pageable, mainPost)
                .map(BoardPostDTO.Response::new);
    }

    public BoardPostDTO.Response findById(Long mainPostId, Long id) {
        mainPostRepository.findById(mainPostId).orElseThrow(() -> new NoSuchElementException("Main post not found"));
        return new BoardPostDTO.Response(boardPostRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("There is no Board post with this ID")));
    }

    // Update
    public BoardPostDTO.Response update(BoardPostDTO.Update update, Long mainPostId, Long id) {
        mainPostRepository.findById(mainPostId).orElseThrow(() -> new NoSuchElementException("Main post not found"));
        BoardPost oldBoardPost = boardPostRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("There is no Board post with this ID"));
        BoardPost newBoardPost = BoardPost.builder()
                .id(id)
                .mainPost(oldBoardPost.getMainPost())
                .title(update.getTitle())
                .author(oldBoardPost.getAuthor())
                .context(update.getContext())
                .comments(oldBoardPost.getComments())
                .build();
        boardPostRepository.save(newBoardPost);
        return this.findById(newBoardPost.getMainPost().getId(), id);
    }

    public ResponseEntity<String> delete(Long mainPostId, Long id) {
        mainPostRepository.findById(mainPostId).orElseThrow(() -> new NoSuchElementException("Main post not found"));
        try {
            boardPostRepository.deleteById(id);
        } catch (Exception e) {
            return new ResponseEntity<String>("Exception occurred", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("Delete success");
    }
}
