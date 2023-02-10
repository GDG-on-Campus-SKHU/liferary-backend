package gdsc.skhu.liferary.controller;

import gdsc.skhu.liferary.domain.DTO.SignUpDTO;
import gdsc.skhu.liferary.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SignUpController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignUpDTO signUpDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
           log.info("SignUp Fail");
        }
        memberService.signup(signUpDTO);
        return ResponseEntity.ok("SignUp success");
    }
}
