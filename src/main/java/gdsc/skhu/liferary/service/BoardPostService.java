package gdsc.skhu.liferary.service;

import gdsc.skhu.liferary.domain.BoardPost;
import gdsc.skhu.liferary.domain.DTO.BoardPostDTO;
import gdsc.skhu.liferary.domain.DTO.ImageDTO;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BoardPostService {
    private final MainPostRepository mainPostRepository;
    private final BoardPostRepository boardPostRepository;
    private final MemberRepository memberRepository;
    private final ImageService imageService;

    // Create
    @Transactional
    public BoardPostDTO.Response save(BoardPostDTO.Request request) throws IOException {
        BoardPost boardPost = BoardPost.builder()
                .mainPost(mainPostRepository.findById(request.getMainPostId())
                        .orElseThrow(() -> new NoSuchElementException("Main post not found")))
                .title(request.getTitle())
                .author(memberRepository.findByEmail(request.getAuthor())
                        .orElseThrow(() -> new NoSuchElementException("Member not found")))
                .context(request.getContext())
                .images(new ArrayList<>())
                .build();
        if(request.getImages() != null) {
            for(MultipartFile file : request.getImages()) {
                ImageDTO.Response image = imageService.uploadImage("board/", file);
                boardPost.getImages().add(image.getImagePath());
            }
        }
        return new BoardPostDTO.Response(boardPostRepository.save(boardPost));
    }

    //Read
    @Transactional
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
    @Transactional
    public BoardPostDTO.Response update(BoardPostDTO.Update update, Long mainPostId, Long id) throws IOException {
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
                .images(new ArrayList<>())
                .build();
        if(update.getImages() != null) {
            for(MultipartFile file : update.getImages()) {
                ImageDTO.Response image = imageService.uploadImage("/board/", file);
                newBoardPost.getImages().add(image.getImagePath());
            }
        }
        boardPostRepository.save(newBoardPost);
        return this.findById(newBoardPost.getMainPost().getId(), id);
    }

    @Transactional
    public ResponseEntity<String> delete(Long mainPostId, Long id) {
        mainPostRepository.findById(mainPostId).orElseThrow(() -> new NoSuchElementException("Main post not found"));
        try {
            BoardPost boardPost = boardPostRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Board post not found"));
            if(!boardPost.getImages().isEmpty()) {
                for(String path : boardPost.getImages()) {
                    imageService.deleteImage(path);
                }
            }
            boardPostRepository.deleteById(id);
        } catch (Exception e) {
            return new ResponseEntity<>("Exception occurred", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("Delete success");
    }
}
