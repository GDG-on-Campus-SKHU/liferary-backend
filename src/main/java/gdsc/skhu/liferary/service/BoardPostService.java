package gdsc.skhu.liferary.service;

import gdsc.skhu.liferary.domain.BoardPost;
import gdsc.skhu.liferary.domain.DTO.BoardPostDTO;
import gdsc.skhu.liferary.domain.DTO.ImageDTO;
import gdsc.skhu.liferary.domain.MainPost;
import gdsc.skhu.liferary.domain.Member;
import gdsc.skhu.liferary.repository.boardpost.BoardPostRepository;
import gdsc.skhu.liferary.repository.mainpost.MainPostRepository;
import gdsc.skhu.liferary.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BoardPostService {
    private final MainPostRepository mainPostRepository;
    private final BoardPostRepository boardPostRepository;
    private final MemberRepository memberRepository;
    private final ImageService imageService;
    private final EntityManager entityManager;

    // Create
    public BoardPostDTO.Response save(String username, BoardPostDTO.Request request) throws IOException {
        BoardPost boardPost = BoardPost.builder()
                .mainPost(mainPostRepository.findById(request.getMainPostId())
                        .orElseThrow(() -> new NoSuchElementException("Main post not found")))
                .title(request.getTitle())
                .author(memberRepository.findByEmail(username)
                        .orElseThrow(() -> new NoSuchElementException("Member not found")))
                .context(request.getContext())
                .images(new ArrayList<>())
                .build();
        saveWithImage(boardPost, request.getImages());
        return new BoardPostDTO.Response(boardPost);
    }

    //Read
    @Transactional(readOnly = true)
    public Page<BoardPostDTO.Response> findAll(Pageable pageable) {
        return boardPostRepository.findAll(pageable).map(boardPost -> {
            if(boardPost.getImages() != null) {
                boardPost.getImages().replaceAll(
                        storedImageName -> imageService.findByStoredImageName(storedImageName).getImagePath()
                );
            }
            return boardPost;
        }).map(BoardPostDTO.Response::new);
    }

    @Transactional(readOnly = true)
    public Page<BoardPostDTO.Response> findByMainPost(Pageable pageable, Long mainPostId) {
        MainPost mainPost = mainPostRepository.findById(mainPostId)
                .orElseThrow(() -> new NoSuchElementException("Main post not found"));
        return boardPostRepository.findByMainPost(pageable, mainPost).map(boardPost -> {
            if(boardPost.getImages() != null) {
                boardPost.getImages().replaceAll(
                        storedImageName -> imageService.findByStoredImageName(storedImageName).getImagePath()
                );
            }
            return boardPost;
        }).map(BoardPostDTO.Response::new);
    }

    @Transactional(readOnly = true)
    public BoardPostDTO.Response findById(Long mainPostId, Long id) {
        mainPostRepository.findById(mainPostId).orElseThrow(() -> new NoSuchElementException("Main post not found"));
        return new BoardPostDTO.Response(boardPostRepository.findById(id).map(boardPost -> {
            if(boardPost.getImages() != null) {
                boardPost.getImages().replaceAll(
                        storedImageName -> imageService.findByStoredImageName(storedImageName).getImagePath()
                );
            }
            return boardPost;
        }).orElseThrow(() -> new NoSuchElementException("There is no Board post with this ID")));
    }

    @Transactional(readOnly = true)
    public Page<BoardPostDTO.Response> findByMember(Pageable pageable, String username) {
        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new NoSuchElementException("Member not found"));
        return boardPostRepository.findByAuthor(pageable, member).map(boardPost -> {
            if(boardPost.getImages() != null) {
                boardPost.getImages().replaceAll(
                        storedImageName -> imageService.findByStoredImageName(storedImageName).getImagePath()
                );
            }
            return boardPost;
        }).map(BoardPostDTO.Response::new);
    }

    @Transactional(readOnly = true)
    public Page<BoardPostDTO.Response> findByMainPostAndKeyword(Pageable pageable, Long mainPostId, String keyword) {
        MainPost mainPost = mainPostRepository.findById(mainPostId)
                .orElseThrow(() -> new NoSuchElementException("Main post not found"));
        return boardPostRepository.findByKeyword(pageable, mainPost, keyword).map(boardPost -> {
            if(boardPost.getImages() != null) {
                boardPost.getImages().replaceAll(
                        storedImageName -> imageService.findByStoredImageName(storedImageName).getImagePath()
                );
            }
            return boardPost;
        });
    }

    // Update
    public BoardPostDTO.Response update(String username, BoardPostDTO.Update update, Long mainPostId, Long id) throws IOException {
        mainPostRepository.findById(mainPostId).orElseThrow(() -> new NoSuchElementException("Main post not found"));
        BoardPost oldBoardPost = boardPostRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("There is no Board post with this ID"));
        BoardPost newBoardPost;
        if(oldBoardPost.getAuthor().getEmail().equals(username)) {
            newBoardPost = BoardPost.builder()
                    .id(id)
                    .mainPost(oldBoardPost.getMainPost())
                    .title(update.getTitle())
                    .author(oldBoardPost.getAuthor())
                    .context(update.getContext())
                    .comments(oldBoardPost.getComments())
                    .images(new ArrayList<>())
                    .build();
        } else {
            throw new AccessDeniedException("Unauthorized access");
        }
        saveWithImage(newBoardPost, update.getImages());
        return new BoardPostDTO.Response(newBoardPost);
    }

    @Transactional
    public ResponseEntity<String> delete(Long mainPostId, Long id) {
        mainPostRepository.findById(mainPostId).orElseThrow(() -> new NoSuchElementException("Main post not found"));
        try {
            BoardPost boardPost = boardPostRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Board post not found"));
            if(boardPost.getImages() != null) {
                for(String imageName : boardPost.getImages()) {
                    ImageDTO.Response image = imageService.findByStoredImageName(imageName);
                    imageService.deleteImage("board/", image.getImagePath());
                }
            }
            boardPostRepository.deleteById(id);
        } catch (Exception e) {
            return new ResponseEntity<>("Exception occurred", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("Delete success");
    }

    // Util
    private void saveWithImage(BoardPost boardPost, List<String> images) {
        if(images != null) {
            for (String imagePath : images) {
                ImageDTO.Response image = imageService.findByImagePath(imagePath);
                boardPost.getImages().add(image.getStoredImageName());
            }
        }
        boardPostRepository.saveAndFlush(boardPost);
        entityManager.detach(boardPost);
        if(boardPost.getImages() != null) {
            boardPost.getImages().replaceAll(storedImageName -> imageService.findByStoredImageName(storedImageName).getImagePath());
        }
    }
}
