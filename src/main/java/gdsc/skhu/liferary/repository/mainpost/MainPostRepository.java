package gdsc.skhu.liferary.repository.mainpost;

import gdsc.skhu.liferary.domain.Category;
import gdsc.skhu.liferary.domain.MainPost;
import gdsc.skhu.liferary.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MainPostRepository extends JpaRepository<MainPost, Long>, MainPostCustomRepository {
    Page<MainPost> findByCategory(Pageable pageable, Category category);
    Page<MainPost> findByAuthor(Pageable pageable, Member member);
}
