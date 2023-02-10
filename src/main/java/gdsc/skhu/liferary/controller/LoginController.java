package gdsc.skhu.liferary.controller;

import gdsc.skhu.liferary.domain.DTO.LoginDTO;
import gdsc.skhu.liferary.domain.DTO.TokenDTO;
import gdsc.skhu.liferary.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final MemberService memberService;

    @PostMapping("/login")
    public TokenDTO login(@RequestBody LoginDTO loginRequestDTO) {
        return memberService.login(loginRequestDTO);
    }
}
