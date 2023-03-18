package gdsc.skhu.liferary.token;

import com.google.firebase.auth.FirebaseToken;
import gdsc.skhu.liferary.service.TokenUserDetailsService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
public class FirebaseFilter extends GenericFilterBean {
    private final TokenUserDetailsService tokenUserDetailsService;
    private final TokenProvider tokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        FirebaseToken firebaseToken;
        try {
            firebaseToken = tokenProvider.getFirebaseToken(request);
            tokenUserDetailsService.saveUser(firebaseToken);
            UserDetails user = tokenUserDetailsService.loadUserByUsername(firebaseToken.getEmail());
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            request.setAttribute("isFirebaseToken", true);
            chain.doFilter(request, response);
        } catch (NoSuchElementException | IllegalArgumentException e) {
            request.setAttribute("isFirebaseToken", false);
            chain.doFilter(request, response);
        }
    }
}