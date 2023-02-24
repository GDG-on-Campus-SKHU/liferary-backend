package gdsc.skhu.liferary.token;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import gdsc.skhu.liferary.service.TokenUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
public class FirebaseFilter extends GenericFilterBean {
    private final TokenUserDetailsService tokenUserDetailsService;
    private final TokenProvider tokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        FirebaseToken firebaseToken = tokenProvider.getFirebaseToken(request);

        try {
            tokenUserDetailsService.saveUser(firebaseToken);
            UserDetails user = tokenUserDetailsService.loadUserByUsername(firebaseToken.getEmail());
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (NoSuchElementException e) {
            chain.doFilter(request, response);
            return;
        }
        chain.doFilter(request, response);
    }
}
