package gdsc.skhu.liferary.service;

import gdsc.skhu.liferary.configure.cache.CacheKey;
import com.google.firebase.auth.FirebaseToken;

import gdsc.skhu.liferary.domain.Category;
import gdsc.skhu.liferary.domain.DTO.MainPostDTO;
import gdsc.skhu.liferary.domain.DTO.MemberDTO;
import gdsc.skhu.liferary.domain.DTO.TokenDTO;
import gdsc.skhu.liferary.domain.LogoutAccessToken;
import gdsc.skhu.liferary.domain.MainPost;
import gdsc.skhu.liferary.domain.Member;
import gdsc.skhu.liferary.repository.LogoutAccessTokenRedisRepository;
import gdsc.skhu.liferary.token.TokenProvider;
import gdsc.skhu.liferary.repository.MemberRepository;
import gdsc.skhu.liferary.repository.RefreshTokenRedisRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;

    //User Join
    @Transactional
    public MemberDTO.Response join(MemberDTO.@Valid Join joinRequestDto) {
        if (memberRepository.findByEmail(joinRequestDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Duplicated email");
        }
        if (!joinRequestDto.getPassword().equals(joinRequestDto.getCheckedPassword())) {
            throw new IllegalArgumentException("Password mismatch");
        }
        joinRequestDto.setPassword(passwordEncoder.encode(joinRequestDto.getPassword()));
        Member member = memberRepository.saveAndFlush(Member.ofUser(joinRequestDto));

        return new MemberDTO.Response(memberRepository.findById(member.getId())
                .orElseThrow(() -> new NoSuchElementException("Member not found")));
    }

    //Admin Join
    @Transactional
    public MemberDTO.Response joinAdmin(MemberDTO.@Valid Join joinRequestDto) {
        if (memberRepository.findByEmail(joinRequestDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Duplicated email");
        }
        if (!joinRequestDto.getPassword().equals(joinRequestDto.getCheckedPassword())) {
            throw new IllegalArgumentException("Password mismatch");
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

    //Login
    @Transactional
    public TokenDTO login(MemberDTO.Login login) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return tokenProvider.createToken(authentication);
    }
    
    //Firebase Login
    @Transactional
    public MemberDTO.Response login(FirebaseToken firebaseToken) {
        if(memberRepository.findByEmail(firebaseToken.getEmail()).isPresent()) {
            return new MemberDTO.Response(memberRepository.findByEmail(firebaseToken.getEmail())
                    .orElseThrow(() -> new NoSuchElementException("Member not found")));
        }

        String password = passwordEncoder.encode(UUID.randomUUID().toString());
        return join(MemberDTO.Join.builder()
                .email(firebaseToken.getEmail())
                .nickname(firebaseToken.getName())
                .password(password)
                .checkedPassword(password)
                .firebaseAuth(true)
                .build());
    }

    public MemberDTO.Response findByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Member not found"));
        return new MemberDTO.Response(member);
    }

    // Logout
    @CacheEvict(value = CacheKey.USER, key = "#username")
    public ResponseEntity<String> logout(TokenDTO tokenDto, String username) {
        String accessToken = tokenProvider.resolveToken(tokenDto.getAccessToken());
        long remainMilliSeconds = tokenProvider.getRemainMilliSeconds(accessToken);
        refreshTokenRedisRepository.deleteById(username);
        logoutAccessTokenRedisRepository.save(LogoutAccessToken.of(accessToken, username, remainMilliSeconds));
        return ResponseEntity.ok("Logout success");
    }

    //Update
    @Transactional
    public MemberDTO.Response update(String username, MemberDTO.Update update) throws IOException {
        Member preMember = memberRepository.findByEmail(username)
                .orElseThrow(() -> new NoSuchElementException("Not found Member"));
        Member modMember;
        modMember = Member.builder()
                .id(preMember.getId())
                .email(preMember.getEmail())
                .authority(preMember.getAuthority())
                .nickname(update.getNickname())
                .password(passwordEncoder.encode(update.getPassword()))
                .build();

        if(update.getPassword().equals(update.getCheckedPassword())){
            memberRepository.save(modMember);
        } else {
            throw new IllegalArgumentException("Password check failed");
        }
        return this.findByEmail(username);
    }


    //Delete
    @CacheEvict(value = CacheKey.USER, key = "#p1")
    public ResponseEntity<String> withdraw(String withdrawPassword, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Member not found"));

        if (!member.matchPassword(passwordEncoder, withdrawPassword)) {
            throw new IllegalArgumentException("Miss Match password");
        }
        memberRepository.delete(member);
        return ResponseEntity.ok("Withdraw success");
    }
}