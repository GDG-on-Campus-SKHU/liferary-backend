package gdsc.skhu.liferary.controller;

import gdsc.skhu.liferary.domain.DTO.LoginDTO;
import gdsc.skhu.liferary.domain.DTO.SignUpDTO;
import gdsc.skhu.liferary.domain.DTO.TokenDTO;
import gdsc.skhu.liferary.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;


    //create
    @PostMapping("/sign-up")
    public String signup(@Valid @RequestBody SignUpDTO signUpDTO, Errors errors, Model model) {

        if (errors.hasErrors()) {
            /* 회원가입 실패시 입력 데이터 값을 유지 */
            model.addAttribute("signUpDTO", signUpDTO);

            /* 유효성 통과 못한 필드와 메시지를 핸들링 */
            Map<String, String> validatorResult = memberService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }

            return "/sign-up";
        }
        memberService.checkEmailDuplication(signUpDTO);

        memberService.signup(signUpDTO);
        return "redirect:/login";
    }

    @PostMapping("/login")
    public TokenDTO login(@RequestBody LoginDTO LoginRequestDto) {
        String email = LoginRequestDto.getEmail();
        String password = LoginRequestDto.getPassword();
        TokenDTO tokenDTO = memberService.login(email, password);
        return tokenDTO;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(Principal principal, @PathVariable Long id) {
        LoginDTO loginDTO = memberService.findById(id);
        if(principal.getName().equals(loginDTO.getEmail())) {
            memberService.withdraw(id);
            return ResponseEntity.ok("Delete member success");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED");
    }

}
