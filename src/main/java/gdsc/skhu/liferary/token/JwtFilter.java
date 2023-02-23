package gdsc.skhu.liferary.jwt;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import gdsc.skhu.liferary.service.JwtUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
import java.util.NoSuchElementException;

@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {
    private final JwtUserDetailsService jwtUserDetailsService;
    private final FirebaseAuth firebaseAuth;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = resolveToken((HttpServletRequest) request); //request header에서 jwt 토큰 추출
        FirebaseToken firebaseToken;
        try {
            firebaseToken = firebaseAuth.verifyIdToken(token);
        } catch (FirebaseAuthException e) {
            chain.doFilter(request, response);
            throw new RuntimeException(e);
        }

        try {
            jwtUserDetailsService.save(firebaseToken);
            UserDetails user = jwtUserDetailsService.loadUserByUsername(firebaseToken.getEmail());
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (NoSuchElementException e) {
            throw new RuntimeException(e);
        }
        chain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
