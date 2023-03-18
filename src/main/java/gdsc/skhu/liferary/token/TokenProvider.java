package gdsc.skhu.liferary.token;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import gdsc.skhu.liferary.domain.DTO.TokenDTO;
import gdsc.skhu.liferary.domain.RefreshToken;
import gdsc.skhu.liferary.jwt.JwtExpirationEnums;
import gdsc.skhu.liferary.repository.MemberRepository;
import gdsc.skhu.liferary.repository.RefreshTokenRedisRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static gdsc.skhu.liferary.jwt.JwtExpirationEnums.ACCESS_TOKEN_EXPIRATION_TIME;
import static gdsc.skhu.liferary.jwt.JwtExpirationEnums.REFRESH_TOKEN_EXPIRATION_TIME;

@Slf4j
@Component
public class TokenProvider {
    private final Key key;
    private final FirebaseAuth firebaseAuth;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final MemberRepository memberRepository;

    public TokenProvider(
            @Value("${jwt.secret}") String secretKey,
            FirebaseAuth firebaseAuth,
            RefreshTokenRedisRepository refreshTokenRedisRepository,
            MemberRepository memberRepository) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.firebaseAuth = firebaseAuth;
        this.refreshTokenRedisRepository = refreshTokenRedisRepository;
        this.memberRepository = memberRepository;
    }

    //token 생성
    public TokenDTO createToken(Authentication authentication) {
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String username = principal.getUsername();

        String accessToken = generateAccessToken(username);
        String refreshToken = saveRefreshToken(username).getRefreshToken();

        return TokenDTO.of(accessToken, refreshToken);
    }

    //복호화해서 토큰에 들어있는 정보 꺼내는 메서드
    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        //UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.get("username").toString(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
            throw new JwtException("Invalid JWT Token");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
            throw new JwtException("Expired JWT Token");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
            throw new JwtException("Unsupported JWT Token");
        } catch (IllegalArgumentException e) {
            log.info("Claims not found JWT Token", e);
            throw new JwtException("Claims not found JWT Token");
        }
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String resolveToken(String token) {
        return token.substring(7);
    }

    private RefreshToken saveRefreshToken(String username) {
        return refreshTokenRedisRepository.save(
                RefreshToken.createRefreshToken(
                        username,
                        generateRefreshToken(username),
                        REFRESH_TOKEN_EXPIRATION_TIME.getValue()
                )
        );
    }

    //토큰 재발급
    public TokenDTO reissue(String refreshToken) {
        Claims claims = parseClaims(refreshToken);
        String username = claims.get("username").toString();
        RefreshToken redisRefreshToken = refreshTokenRedisRepository.findById(username)
                .orElseThrow(NoSuchElementException::new);

        if (refreshToken.equals(redisRefreshToken.getRefreshToken())) {
            return reissueToken(refreshToken);
        }
        throw new IllegalArgumentException("토큰이 일치하지 않습니다.");
    }

    private String getCurrentUsername(Authentication authentication) {
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        return principal.getUsername();
    }

    private String getCurrentUserAuthorities(String username) {
        return memberRepository.findByEmail(username)
                .orElseThrow(() -> new NoSuchElementException("Member not found")).getAuthority().toString();
    }

    private TokenDTO reissueToken(String refreshToken) {
        Claims claims = parseClaims(refreshToken);
        String username = claims.get("username").toString();
        if (lessThanReissueExpirationTimesLeft(refreshToken)) {
            String accessToken = generateAccessToken(username);
            return TokenDTO.of(accessToken, saveRefreshToken(username).getRefreshToken());
        }
        return TokenDTO.of(generateAccessToken(username), refreshToken);
    }

    private boolean lessThanReissueExpirationTimesLeft(String refreshToken) {
        return getRemainMilliSeconds(refreshToken) < JwtExpirationEnums.REISSUE_EXPIRATION_TIME.getValue();
    }

    public FirebaseToken getFirebaseToken(ServletRequest request) {
        String token;
        FirebaseToken firebaseToken;
        try {
            token = resolveToken((HttpServletRequest) request);
            firebaseToken = firebaseAuth.verifyIdToken(token);
        } catch (IllegalArgumentException | FirebaseAuthException e) {
            throw new IllegalArgumentException("Not supported Firebase access token");
        }
        return firebaseToken;
    }

    public String getUsername(String token) {
        return parseClaims(token).get("username", String.class);
    }

    public String generateAccessToken(String username) {
        Claims claims = Jwts.claims();
        claims.put("username", username);
        claims.put("auth", getCurrentUserAuthorities(username));

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME.getValue()))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(String username) {
        Claims claims = Jwts.claims();
        claims.put("username", username);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME.getValue()))
                .signWith(key)
                .compact();
    }

    public long getRemainMilliSeconds(String token) {
        Date expiration = parseClaims(token).getExpiration();
        Date now = new Date();
        return expiration.getTime() - now.getTime();
    }
}