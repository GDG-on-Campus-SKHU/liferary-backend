package gdsc.skhu.liferary.service;

import com.google.firebase.auth.FirebaseToken;
import gdsc.skhu.liferary.domain.DTO.MemberDTO;
import gdsc.skhu.liferary.domain.DTO.OAuth2Attribute;
import gdsc.skhu.liferary.domain.Member;
import gdsc.skhu.liferary.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByEmail(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));
    }

    // 해당하는 User 의 데이터가 존재한다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createUserDetails(Member member) {
        return User.builder()
                .username(member.getUsername())
                .password(member.getPassword())
                .roles(member.getRoles().toArray(new String[0]))
                .build();
    }

    public MemberDTO.Response save(FirebaseToken firebaseToken) {
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        Member member = memberRepository.findByEmail(firebaseToken.getEmail())
                .orElse(Member.builder()
                        .email(firebaseToken.getEmail())
                        .nickname(firebaseToken.getName())
                        .password("password")
                        .roles(roles)
                        .build());
        return new MemberDTO.Response(memberRepository.save(member));
    }
}