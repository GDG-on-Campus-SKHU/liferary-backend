package gdsc.skhu.liferary.repository;

import gdsc.skhu.liferary.domain.MainPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MainPostRepository extends JpaRepository<MainPost, Long> {
}
