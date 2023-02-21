package gdsc.skhu.liferary.controller;

import gdsc.skhu.liferary.domain.DTO.TokenDTO;
import gdsc.skhu.liferary.jwt.TokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@Tag(name = "OAuth2", description = "API for authentication and authorization")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/oauth2")
public class OAuth2Controller {
    private final TokenProvider tokenProvider;

    @Operation(summary = "oauth2 token", description = "Login with Google OAuth2")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping("/token")
    public ResponseEntity<TokenDTO> token(@AuthenticationPrincipal OAuth2User oAuth2User) {
        System.out.println("authentication = " + oAuth2User.getAttributes());
        ResponseEntity<TokenDTO> response =  ResponseEntity.ok(tokenProvider.createOAuthToken(oAuth2User));
        return response;
    }
}
