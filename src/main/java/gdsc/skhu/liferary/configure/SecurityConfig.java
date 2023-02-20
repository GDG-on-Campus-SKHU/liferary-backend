package gdsc.skhu.liferary.configure;

import gdsc.skhu.liferary.handler.OAuth2SuccessHandler;
import gdsc.skhu.liferary.jwt.JwtFilter;
import gdsc.skhu.liferary.jwt.TokenProvider;

import gdsc.skhu.liferary.service.Oauth2MemberSerivce;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    private final Oauth2MemberSerivce oauth2MemberSerivce;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private static final String[] PERMITTED_URLS = {
            /* Swagger v2 */
            "/v2/api-docs",
            "/v2/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            /* Swagger v3 */
            "/api-docs/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            /* Login API */
            "/api/member/**",
            /* Static objects */
            "/favicon.ico"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(PERMITTED_URLS).permitAll()
//                .antMatchers("api/user/**").hasAnyRole("USER", "ADMIN")
//                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                .oauth2Login()
                .defaultSuccessUrl("/api/oauth2/token")
                .successHandler(oAuth2SuccessHandler)
                .userInfoEndpoint()
                .userService(oauth2MemberSerivce);

        http.addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://front-server.com");
            }
        };
    }

}
