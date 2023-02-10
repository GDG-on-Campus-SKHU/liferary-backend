package gdsc.skhu.liferary.service;

import gdsc.skhu.liferary.domain.DTO.LoginDTO;
import gdsc.skhu.liferary.domain.DTO.SignUpDTO;
import gdsc.skhu.liferary.domain.DTO.TokenDTO;
import gdsc.skhu.liferary.jwt.TokenProvider;
import gdsc.skhu.liferary.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public void signup(SignUpDTO signUpRequestDTO) {
        if(memberRepository.findByEmail(signUpRequestDTO.getEmail()).isPresent()) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }
        if(!signUpRequestDTO.getPassword().equals(signUpRequestDTO.getCheckedPassword())) {
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }
        signUpRequestDTO.setPassword(passwordEncoder.encode(signUpRequestDTO.getPassword()));
        memberRepository.save(signUpRequestDTO.toEntity());
    }

    @Transactional
    public TokenDTO login(LoginDTO loginRequestDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return tokenProvider.createToken(authentication);
    }
}
