package gdsc.skhu.liferary.repository;

import gdsc.skhu.liferary.domain.BoardPost;
import gdsc.skhu.liferary.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBoardPost(Pageable pageable, BoardPost boardPost);
}
