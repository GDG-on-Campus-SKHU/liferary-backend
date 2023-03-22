package gdsc.skhu.liferary.controller;

import com.google.firebase.auth.FirebaseToken;
import gdsc.skhu.liferary.domain.DTO.MemberDTO;
import gdsc.skhu.liferary.service.MemberService;
import gdsc.skhu.liferary.token.TokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;

@Tag(name = "Member", description = "API for authentication and authorization")
@RequiredArgsConstructor
@RequestMapping("/api/firebase")
@RestController
public class FirebaseController {
    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    // Create
    @Operation(summary = "firebase login", description = "Login with Firebase")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PostMapping("/join")
    public MemberDTO.Response join(ServletRequest request) {
        FirebaseToken firebaseToken = tokenProvider.getFirebaseToken(request);
        return memberService.login(firebaseToken);
    }

    // Read
    @Operation(summary = "firebase user info", description = "Read user info")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping("/info")
    public MemberDTO.Response getInfo(@AuthenticationPrincipal UserDetails userDetails) {
        return memberService.findByEmail(userDetails.getUsername());
    }
}
