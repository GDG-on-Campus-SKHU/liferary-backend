package gdsc.skhu.liferary.configure;

import gdsc.skhu.liferary.repository.LogoutAccessTokenRedisRepository;
import gdsc.skhu.liferary.token.FirebaseFilter;
import gdsc.skhu.liferary.token.JwtFilter;
import gdsc.skhu.liferary.token.TokenProvider;

import gdsc.skhu.liferary.service.TokenUserDetailsService;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenUserDetailsService tokenUserDetailsService;
    private final TokenProvider tokenProvider;
    private final LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;
    private static final String[] PERMITTED_URLS = {
            "/api/main/**",
            "/api/board/**",
            "/api/study/**",
            "/api/comment/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .logout().disable()
                .formLogin().disable()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers(HttpMethod.GET, PERMITTED_URLS).permitAll()
                .anyRequest().authenticated();

        http
                .addFilterBefore(new JwtFilter(tokenProvider, tokenUserDetailsService, logoutAccessTokenRedisRepository), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new FirebaseFilter(tokenUserDetailsService, tokenProvider), JwtFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedOrigin("https://liferary-frontend.vercel.app");
        configuration.addAllowedOrigin("http://api-liferary.duckdns.org");
        configuration.setAllowedMethods(List.of(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PATCH.name(),
                HttpMethod.DELETE.name()
        ));
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .antMatchers(HttpMethod.GET, "/favicon.ico")
                .antMatchers("/v2/api-docs",
                        "/v2/api-docs/**",
                        "/swagger-resources",
                        "/swagger-resources/**",
                        "/configuration/ui",
                        "/configuration/security",
                        "/swagger-ui.html",
                        "/webjars/**",
                        "/api-docs/**",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/api/member/join",
                        "/api/member/login")
                .antMatchers(HttpMethod.POST, "/api/firebase/login");
    }
}
