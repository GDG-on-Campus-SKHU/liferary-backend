package gdsc.skhu.liferary.service;

import com.google.firebase.auth.FirebaseToken;
import gdsc.skhu.liferary.configure.cache.CacheKey;
import gdsc.skhu.liferary.domain.DTO.MemberDTO;
import gdsc.skhu.liferary.domain.Member;
import gdsc.skhu.liferary.domain.RedisUserDetails;
import gdsc.skhu.liferary.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @Override
    @Cacheable(value = CacheKey.USER, key = "#username", unless = "#result == null")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByEmail(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));
    }

    // 해당하는 User 의 데이터가 존재한다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createUserDetails(Member member) {
        return RedisUserDetails.of(member);
    }

    public MemberDTO.Response saveUser(FirebaseToken firebaseToken) {
        return memberService.login(firebaseToken);
    }
}
