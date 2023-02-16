package gdsc.skhu.liferary.service;

import gdsc.skhu.liferary.domain.BoardPost;
import gdsc.skhu.liferary.domain.Comment;
import gdsc.skhu.liferary.domain.DTO.CommentDTO;
import gdsc.skhu.liferary.repository.BoardPostRepository;
import gdsc.skhu.liferary.repository.CommentRepository;
import gdsc.skhu.liferary.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
    public CommentDTO.Response save(CommentDTO.Request request) {
        Comment comment = Comment.builder()
                .boardPost(boardPostRepository.findById(request.getBoardPostId())
                        .orElseThrow(() -> new NoSuchElementException("Board post not found")))
                .writer(memberRepository.findByEmail(request.getWriter())
                        .orElseThrow(() -> new NoSuchElementException("Member not found")))
                .context(request.getContext())
                .childComments(new ArrayList<>())
                .build();
        return new CommentDTO.Response(commentRepository.save(comment));
    }

    public CommentDTO.Response reply(CommentDTO.Reply reply) {
        Comment comment = Comment.builder()
                .boardPost(boardPostRepository.findById(reply.getBoardPostId())
                        .orElseThrow(() -> new NoSuchElementException("Board post not found")))
                .parentComment(commentRepository.findById(reply.getParentCommentId())
                        .orElseThrow(() -> new NoSuchElementException("Comment not found")))
                .writer(memberRepository.findByEmail(reply.getWriter())
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
    public CommentDTO.Response update(CommentDTO.Update update, Long boardPostId, Long id) {
        boardPostRepository.findById(boardPostId)
                .orElseThrow(() -> new NoSuchElementException("Board post not found"));
        Comment oldComment = this.findById(id);
        Comment newComment = Comment.builder()
                .id(id)
                .boardPost(oldComment.getBoardPost())
                .writer(oldComment.getWriter())
                .context(update.getContext())
                .childComments(oldComment.getChildComments())
                .build();
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
