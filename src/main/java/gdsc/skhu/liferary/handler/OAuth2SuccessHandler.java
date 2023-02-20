package gdsc.skhu.liferary.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import gdsc.skhu.liferary.domain.DTO.MemberDTO;
import gdsc.skhu.liferary.domain.DTO.TokenDTO;
import gdsc.skhu.liferary.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final ObjectMapper objectMapper;
    private final TokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        TokenDTO tokenDTO = tokenProvider.createToken(authentication);
        writeTokenResponse(response, tokenDTO);
    }

    private void writeTokenResponse(HttpServletResponse response, TokenDTO tokenDTO) throws IOException{
        response.setContentType("text/html;charset=UTF-8");

        response.addHeader("Access Token", tokenDTO.getAccessToken());
        response.addHeader("Refresh Token", tokenDTO.getRefreshToken());
        response.setContentType("application/json;charset=UTF-8");

        response.getWriter().println(objectMapper.writeValueAsString(tokenDTO));
    }
}
