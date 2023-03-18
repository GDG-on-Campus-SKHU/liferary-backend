package gdsc.skhu.liferary.token;

import com.google.firebase.auth.FirebaseToken;
import gdsc.skhu.liferary.repository.LogoutAccessTokenRedisRepository;
import gdsc.skhu.liferary.service.TokenUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {
    private final TokenProvider tokenProvider;
    private final TokenUserDetailsService tokenUserDetailsService;
    private final LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if(request.getAttribute("isFirebaseToken").equals(true)) {
        } else {
            String token = tokenProvider.resolveToken((HttpServletRequest) request); //request header에서 jwt 토큰 추출
            if (token != null) {
                checkLogout(token);
            }
            if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
                Authentication authentication = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }
    
    private void checkLogout(String token) {
        if (logoutAccessTokenRedisRepository.existsById(token)) {
            throw new IllegalArgumentException("이미 로그아웃된 회원입니다.");
        }
    }
}