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
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BoardPostService {
    public final MainPostRepository mainPostRepository;
    public final BoardPostRepository boardPostRepository;
    public final MemberRepository memberRepository;

    // Create
    public void save(BoardPostDTO.Request request) {
        BoardPost boardPost = BoardPost.builder()
                .mainPost(mainPostRepository.findById(request.getMainPostId())
                        .orElseThrow(() -> new NoSuchElementException("Main post not found")))
                .title(request.getTitle())
                .author(memberRepository.findByUsername(request.getAuthor()))
                .context(request.getContext())
                .build();
        boardPostRepository.save(boardPost);
    }

    //Read
    public Page<BoardPostDTO.Response> findByMainPost(Pageable pageable, Long mainPostId) {
        MainPost mainPost = mainPostRepository.findById(mainPostId)
                .orElseThrow(() -> new NoSuchElementException("Main post not found"));
        return boardPostRepository.findByMainPost(pageable, mainPost)
                .map(BoardPostDTO.Response::new);
    }
}
