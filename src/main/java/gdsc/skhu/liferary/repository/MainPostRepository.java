package gdsc.skhu.liferary.repository;

import gdsc.skhu.liferary.domain.MainPost;
import org.jboss.jandex.Main;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MainPostRepository extends JpaRepository<MainPost, Long> {
    Page<MainPost> findByCategory(Pageable pageable, String category);
    Page<MainPost> findByTitleContainsOrContextContains(Pageable pageable, String title, String context);
}
