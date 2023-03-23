package gdsc.skhu.liferary.repository;

import gdsc.skhu.liferary.domain.MainPost;
import gdsc.skhu.liferary.domain.Member;
import gdsc.skhu.liferary.domain.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudyRepository extends JpaRepository<Study, Long> {
    Optional<Study> findByMainPost(MainPost mainPost);
    Page<Study> findByAuthor(Pageable pageable, Member member);
}
