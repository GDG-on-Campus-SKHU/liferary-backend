package gdsc.skhu.liferary.controller;

import gdsc.skhu.liferary.domain.DTO.MemberDTO;
import gdsc.skhu.liferary.domain.DTO.TokenDTO;
import gdsc.skhu.liferary.service.MemberService;
import gdsc.skhu.liferary.token.TokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@Tag(name = "Member", description = "API for authentication and authorization")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;


    @Operation(summary = "create member", description = "Create member")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PostMapping("/join")
    public ResponseEntity<MemberDTO.Response> join(@RequestBody @Validated MemberDTO.Join joinDto, Errors errors, Model model) {
        if (errors.hasErrors()) {
            System.out.println(errors);
            /* 유효성 통과 못한 필드와 메시지를 핸들링 */
            Map<String, String> validatorResult = memberService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }
            /* 입력한 내용을 유지하고자 응답 DTO에 담아서 보냄 */
            return ResponseEntity.badRequest().body(new MemberDTO.Response(joinDto.toEntity()));
        }
        return ResponseEntity.ok(memberService.join(joinDto));
    }

    @Operation(summary = "create admin member", description = "Create admin member")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PostMapping("/join/admin")
    public ResponseEntity<MemberDTO.Response> joinAdmin(@RequestBody @Validated MemberDTO.Join joinDto, Errors errors, Model model) {
        if (errors.hasErrors()) {
            /* 유효성 통과 못한 필드와 메시지를 핸들링 */
            Map<String, String> validatorResult = memberService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }

            /* 입력한 내용을 유지하고자 응답 DTO에 담아서 보냄 */
            return ResponseEntity.badRequest().body(new MemberDTO.Response(joinDto.toEntity()));
        }
        return ResponseEntity.ok(memberService.joinAdmin(joinDto));
    }

    @Operation(summary = "login", description = "Login with email and password")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody MemberDTO.Login loginDto) {
        return ResponseEntity.ok(memberService.login(loginDto));
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDTO> reissue(@RequestHeader("RefreshToken") String refreshToken) {
        return ResponseEntity.ok(memberService.reissue(refreshToken));
    }

    @PostMapping("/logout")
    public void logout(@RequestHeader("Authorization") String accessToken,
                       @RequestHeader("RefreshToken") String refreshToken) {
        String username = tokenProvider.getUsername(resolveToken(accessToken));
        memberService.logout(TokenDTO.of(accessToken, refreshToken), username);
    }

    private String resolveToken(String accessToken) {
        return accessToken.substring(7);
    }

    @Operation(summary = "delete member", description = "Delete member")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(Principal principal, @PathVariable Long id) {
        MemberDTO.Login loginDTO = memberService.findById(id);
        if(principal.getName().equals(loginDTO.getEmail())) {
            memberService.withdraw(id);
            return ResponseEntity.ok("Delete member success");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED");
    }
}
