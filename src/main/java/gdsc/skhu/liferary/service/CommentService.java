package gdsc.skhu.liferary.service;

import gdsc.skhu.liferary.domain.BoardPost;
import gdsc.skhu.liferary.domain.Comment;
import gdsc.skhu.liferary.domain.DTO.CommentDTO;
import gdsc.skhu.liferary.repository.boardpost.BoardPostRepository;
import gdsc.skhu.liferary.repository.CommentRepository;
import gdsc.skhu.liferary.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final BoardPostRepository boardPostRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    // Create
    @Transactional
    public CommentDTO.Response save(String username, CommentDTO.Request request) {
        Comment comment = Comment.builder()
                .boardPost(boardPostRepository.findById(request.getBoardPostId())
                        .orElseThrow(() -> new NoSuchElementException("Board post not found")))
                .writer(memberRepository.findByEmail(username)
                        .orElseThrow(() -> new NoSuchElementException("Member not found")))
                .context(request.getContext())
                .childComments(new ArrayList<>())
                .build();
        return new CommentDTO.Response(commentRepository.save(comment));
    }

    public CommentDTO.Response reply(String username, CommentDTO.Reply reply) {
        Comment comment = Comment.builder()
                .boardPost(boardPostRepository.findById(reply.getBoardPostId())
                        .orElseThrow(() -> new NoSuchElementException("Board post not found")))
                .parentComment(commentRepository.findById(reply.getParentCommentId())
                        .orElseThrow(() -> new NoSuchElementException("Comment not found")))
                .writer(memberRepository.findByEmail(username)
                        .orElseThrow(() -> new NoSuchElementException("Member not found")))
                .context(reply.getContext())
                .childComments(new ArrayList<>())
                .build();
        return new CommentDTO.Response(commentRepository.save(comment));
    }

    // Read
    public List<CommentDTO.Response> findByBoardPost(Pageable pageable, Long boardPostId) {
        BoardPost boardPost = boardPostRepository.findById(boardPostId)
                .orElseThrow(() -> new NoSuchElementException("Board post not found"));
        List<Comment> comments = commentRepository.findByBoardPost(pageable, boardPost);
        List<CommentDTO.Response> responses = new ArrayList<>();
        comments.forEach(comment -> {
            if(comment.getParentComment() == null) {
                responses.add(new CommentDTO.Response(comment));
            }
        });
        return responses;
    }

    private Comment findById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("Comment not found"));
    }

    //Update
    public CommentDTO.Response update(String username, CommentDTO.Update update, Long boardPostId, Long id) {
        boardPostRepository.findById(boardPostId)
                .orElseThrow(() -> new NoSuchElementException("Board post not found"));
        Comment oldComment = this.findById(id);
        Comment newComment;
        if(oldComment.getWriter().getEmail().equals(username)) {
            newComment = Comment.builder()
                    .id(id)
                    .boardPost(oldComment.getBoardPost())
                    .writer(oldComment.getWriter())
                    .context(update.getContext())
                    .childComments(oldComment.getChildComments())
                    .build();
        } else {
            throw new AuthorizationServiceException("Unauthorized access");
        }
        commentRepository.save(newComment);
        return new CommentDTO.Response(this.findById(newComment.getId()));
    }

    // Delete
    public ResponseEntity<String> delete(Long id) {
        Comment comment = this.findById(id);
        commentRepository.delete(comment);
        return ResponseEntity.ok("Delete success");
    }
}
