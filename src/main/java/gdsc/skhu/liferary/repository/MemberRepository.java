package gdsc.skhu.liferary.repository;

import gdsc.skhu.liferary.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

//    Member findByUsername(String username);

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);
}
