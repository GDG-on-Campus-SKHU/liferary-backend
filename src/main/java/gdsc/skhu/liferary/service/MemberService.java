package gdsc.skhu.liferary.service;

import com.google.firebase.auth.FirebaseToken;
import gdsc.skhu.liferary.configure.cache.CacheKey;
import gdsc.skhu.liferary.domain.DTO.MemberDTO;
import gdsc.skhu.liferary.domain.DTO.TokenDTO;
import gdsc.skhu.liferary.domain.LogoutAccessToken;
import gdsc.skhu.liferary.domain.Member;
import gdsc.skhu.liferary.domain.RefreshToken;
import gdsc.skhu.liferary.jwt.JwtExpirationEnums;
import gdsc.skhu.liferary.repository.LogoutAccessTokenRedisRepository;
import gdsc.skhu.liferary.repository.MemberRepository;
import gdsc.skhu.liferary.repository.RefreshTokenRedisRepository;
import gdsc.skhu.liferary.token.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

import static gdsc.skhu.liferary.jwt.JwtExpirationEnums.REFRESH_TOKEN_EXPIRATION_TIME;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    //user 회원가입
    @Transactional
    public MemberDTO.Response join(MemberDTO.@Valid Join joinRequestDto) {

        if (memberRepository.findByEmail(joinRequestDto.getEmail()).isPresent()) {
            throw new IllegalStateException("Duplicated email");
        }
        if (!joinRequestDto.getPassword().equals(joinRequestDto.getCheckedPassword())) {
            throw new IllegalStateException("Password mismatch");
        }
        joinRequestDto.setPassword(passwordEncoder.encode(joinRequestDto.getPassword()));
        Member member = memberRepository.saveAndFlush(Member.ofUser(joinRequestDto));

        return new MemberDTO.Response(memberRepository.findById(member.getId())
                .orElseThrow(() -> new NoSuchElementException("Member not found")));
    }

    //admin 회원가입
    @Transactional
    public MemberDTO.Response joinAdmin(MemberDTO.Join joinRequestDto) {

        if (memberRepository.findByEmail(joinRequestDto.getEmail()).isPresent()) {
            throw new IllegalStateException("Duplicated email");
        }
        if (!joinRequestDto.getPassword().equals(joinRequestDto.getCheckedPassword())) {
            throw new IllegalStateException("Password mismatch");
        }
        Member member = memberRepository.saveAndFlush(Member.ofAdmin(joinRequestDto));

        return new MemberDTO.Response(memberRepository.findById(member.getId())
                .orElseThrow(() -> new NoSuchElementException("Member not found")));
    }

    /* 회원가입 시, 유효성 체크 */
    @Transactional(readOnly = true)
    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();

        /* 유효성 검사에 실패한 필드 목록을 받음 */
        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }
        return validatorResult;
    }

    //로그인
    @Transactional
    public TokenDTO login(MemberDTO.Login login) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(login.getEmail(),login.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return tokenProvider.createToken(authentication);
    }

//    //login할 때 checkpassword
//    private void checkPassword(String rawPassword, String findMemberPassword) {
//        if (!passwordEncoder.matches(rawPassword, findMemberPassword)) {
//            throw new IllegalArgumentException("Passwords do not match.");
//        }
//    }

    //firebase Login
    @Transactional
    public MemberDTO.Response login(FirebaseToken firebaseToken) {
        if (memberRepository.findByEmail(firebaseToken.getEmail()).isPresent()) {
            return new MemberDTO.Response(memberRepository.findByEmail(firebaseToken.getEmail())
                    .orElseThrow(() -> new NoSuchElementException("Member not found")));
        }

        String password = passwordEncoder.encode(UUID.randomUUID().toString());
        return join(MemberDTO.Join.builder()
                .email(firebaseToken.getEmail())
                .nickname(firebaseToken.getName())
                .password(password)
                .checkedPassword(password)
                .build());
    }

    @Transactional(readOnly = true)
    public MemberDTO.Login findById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Member not found"));
        return MemberDTO.Login.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .build();
    }

    //firebase에서
    public MemberDTO.Response findByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Member not found"));
        return new MemberDTO.Response(member);
    }


    private RefreshToken saveRefreshToken(String username) {
        return refreshTokenRedisRepository.save(RefreshToken.createRefreshToken(username,
                tokenProvider.generateRefreshToken(username), REFRESH_TOKEN_EXPIRATION_TIME.getValue()));
    }


    //로그아웃
    @CacheEvict(value = CacheKey.USER, key = "#username")
    public void logout(TokenDTO tokenDto, String username) {
        String accessToken = resolveToken(tokenDto.getAccessToken());
        long remainMilliSeconds = tokenProvider.getRemainMilliSeconds(accessToken);
        refreshTokenRedisRepository.deleteById(username);
        logoutAccessTokenRedisRepository.save(LogoutAccessToken.of(accessToken, username, remainMilliSeconds));
    }

    private String resolveToken(String token) {
        return token.substring(7);
    }

    //토큰 재발급
    public TokenDTO reissue(String refreshToken) {
        refreshToken = resolveToken(refreshToken);
        String username = getCurrentUsername();
        RefreshToken redisRefreshToken = refreshTokenRedisRepository.findById(username).orElseThrow(NoSuchElementException::new);

        if (refreshToken.equals(redisRefreshToken.getRefreshToken())) {
            return reissueRefreshToken(refreshToken, username);
        }
        throw new IllegalArgumentException("토큰이 일치하지 않습니다.");
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        return principal.getUsername();
    }

    private TokenDTO reissueRefreshToken(String refreshToken, String username) {
        if (lessThanReissueExpirationTimesLeft(refreshToken)) {
            String accessToken = tokenProvider.generateAccessToken(username);
            return TokenDTO.of(accessToken, saveRefreshToken(username).getRefreshToken());
        }
        return TokenDTO.of(tokenProvider.generateAccessToken(username), refreshToken);
    }

    private boolean lessThanReissueExpirationTimesLeft(String refreshToken) {
        return tokenProvider.getRemainMilliSeconds(refreshToken) < JwtExpirationEnums.REISSUE_EXPIRATION_TIME.getValue();
    }

    @Transactional
    public void withdraw(Long id) {
        memberRepository.deleteById(id);
    }

}