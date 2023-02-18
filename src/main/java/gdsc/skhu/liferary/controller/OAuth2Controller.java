package gdsc.skhu.liferary.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Tag(name = "Member", description = "API for authentication and authorization")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/oauth")
public class OAuth2Controller {
    @Operation(summary = "oauth login", description = "Login with OAuth2 Google")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping("/login")
    public void OAuth2Login(HttpServletResponse response) throws IOException {
        response.sendRedirect("http://localhost:8080/oauth2/authroization/google");
    }
}
