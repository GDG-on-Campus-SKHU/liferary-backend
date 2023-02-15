//package gdsc.skhu.liferary.service;
//
//import gdsc.skhu.liferary.domain.DTO.LoginDTO;
//import gdsc.skhu.liferary.domain.DTO.SignUpDTO;
//import gdsc.skhu.liferary.domain.DTO.TokenDTO;
//import gdsc.skhu.liferary.domain.DTO.UserResponseDTO;
//import gdsc.skhu.liferary.domain.Member;
//import gdsc.skhu.liferary.jwt.TokenProvider;
//import gdsc.skhu.liferary.repository.MemberRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.validation.Errors;
//import org.springframework.validation.FieldError;
//
//import java.util.*;
//
//@Service
//@RequiredArgsConstructor
//public class MemberService {
//
//    private final AuthenticationManagerBuilder authenticationManagerBuilder;
//    private final TokenProvider tokenProvider;
//    private final MemberRepository memberRepository;
//    private final PasswordEncoder passwordEncoder;
//
//
//    @Transactional
//    public Long signup(SignUpDTO signUpRequestDTO) {
//        if (memberRepository.findByEmail(signUpRequestDTO.getEmail()).isPresent()) {
//            throw new IllegalStateException("이미 존재하는 이메일입니다.");
//        }
//        if (!signUpRequestDTO.getPassword().equals(signUpRequestDTO.getCheckedPassword())) {
//            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
//        }
//        List<String> roles = new ArrayList<>();
//        roles.add("USER");
//
//        Member member = memberRepository.saveAndFlush(Member.builder()
//                .email(signUpRequestDTO.getEmail())
//                .nickname(signUpRequestDTO.getNickname())
//                .password(passwordEncoder.encode(signUpRequestDTO.getPassword()))
//                .roles(roles).build());
//
//        return member.getId();
//    }
//
//
//    /* 회원가입 시, 유효성 체크 */
//    @Transactional(readOnly = true)
//    public Map<String, String> validateHandling(Errors errors) {
//        Map<String, String> validatorResult = new HashMap<>();
//
//        /* 유효성 검사에 실패한 필드 목록을 받음 */
//        for (FieldError error : errors.getFieldErrors()) {
//            String validKeyName = String.format("valid_%s", error.getField());
//            validatorResult.put(validKeyName, error.getDefaultMessage());
//        }
//        return validatorResult;
//    }
//
//    //email 중복 확인
//    @Transactional(readOnly = true)
//    public void checkEmailDuplication(SignUpDTO dto) {
//        boolean emailDuplicate = memberRepository.existsByEmail(dto.toEntity().getEmail());
//        if (emailDuplicate) {
//            throw new IllegalStateException("이미 존재하는 이메일입니다.");
//        }
//    }
//
//    @Transactional
//    public UserResponseDTO login(String email, String password) {
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
//
//        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
//
//        TokenDTO tokenDTO = tokenProvider.createToken(authentication);
//
//        return tokenDTO;
//    }
//    @Transactional
//    public void withdraw(Long id) {
//        memberRepository.deleteById(id);
//    }
//
//
//    //회원 정보 조회
//    @Transactional(readOnly = true)
//    public LoginDTO findById(Long id) {
//        Member member = memberRepository.findById(id)
//                .orElseThrow(()-> new NoSuchElementException("Not Found Member"));
//        return LoginDTO.builder()
//                .email(member.getEmail())
//                .password(member.getPassword())
//                .build();
//    }
////    @Transactional
////    public void withdraw(String checkPassword) throws Exception {
////        Member member = memberRepository.findById(SecurityUtil.getLoginUsername()).orElseThrow(() -> new Exception("회원이 존재하지 않습니다"));
////
////        if(!member.matchPassword(passwordEncoder, checkPassword) ) {
////            throw new Exception("비밀번호가 일치하지 않습니다.");
////        }
////
////        memberRepository.delete(member);
////    }
//
//
////    //회원 정보 삭제
////    @Transactional
////    public void removeMember(Long id) {
////        memberRepository.deleteById(id);
////    }
//}
