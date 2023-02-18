package gdsc.skhu.liferary.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import gdsc.skhu.liferary.domain.DTO.MemberDTO;
import gdsc.skhu.liferary.domain.DTO.TokenDTO;
import gdsc.skhu.liferary.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final TokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
//        MemberDTO.OAuth2 memberDTO = MemberDTO.OAuth2.builder()
//                .email((String) oAuth2User.getAttributes().get("email"))
//                .nickname((String) oAuth2User.getAttributes().get("name"))
//                .build();
        TokenDTO tokenDTO = tokenProvider.createToken(authentication);
        writeTokenResponse(response, tokenDTO);
    }

    private void writeTokenResponse(HttpServletResponse response, TokenDTO tokenDTO) throws IOException{
        response.setContentType("application/json;charset=UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        String responseBody = objectMapper.writeValueAsString(tokenDTO);
        response.getWriter().write(responseBody);
    }
}
