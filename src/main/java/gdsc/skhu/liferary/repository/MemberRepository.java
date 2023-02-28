package gdsc.skhu.liferary.repository;

import gdsc.skhu.liferary.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

//    @Query("select m from Member m join fetch m.authorities a where m.username = :username")
//    Optional<Member> findByUsernameWithAuthority(String username);
}
