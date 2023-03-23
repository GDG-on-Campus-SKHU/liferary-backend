package gdsc.skhu.liferary.repository.boardpost;

import gdsc.skhu.liferary.domain.BoardPost;
import gdsc.skhu.liferary.domain.MainPost;
import gdsc.skhu.liferary.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardPostRepository extends JpaRepository<BoardPost, Long>, BoardPostCustomRepository {
    Page<BoardPost> findByMainPost(Pageable pageable, MainPost mainPost);
    Page<BoardPost> findByAuthor(Pageable pageable, Member member);
}
