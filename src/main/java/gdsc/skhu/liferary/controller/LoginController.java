//package gdsc.skhu.liferary.controller;
//
//import gdsc.skhu.liferary.domain.DTO.LoginDTO;
//import gdsc.skhu.liferary.domain.DTO.TokenDTO;
//import gdsc.skhu.liferary.service.MemberService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.security.Principal;
//
//@RestController
//@RequiredArgsConstructor
//public class LoginController {
//
//    private final MemberService memberService;
//
////    @PostMapping("/login")
////    public TokenDTO login(@RequestBody LoginDTO LoginRequestDto) {
////        String email = LoginRequestDto.getEmail();
////        String password = LoginRequestDto.getPassword();
////        TokenDTO tokenDTO = memberService.login(email, password);
////        return tokenDTO;
////    }
//
////    @DeleteMapping("/{id}")
////    public ResponseEntity<String> delete(Principal principal, @PathVariable Long id) {
////        LoginDTO loginDTO = memberService.findById(id);
////        if(principal.getName().equals(LoginDTO.())) {
////            postService.delete(id);
////            return ResponseEntity.ok("Delete post success");
////        }
////        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED");
////    }
//}
