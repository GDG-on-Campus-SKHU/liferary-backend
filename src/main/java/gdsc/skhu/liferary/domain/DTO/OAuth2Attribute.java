package gdsc.skhu.liferary.domain.DTO;

import gdsc.skhu.liferary.domain.Member;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2Attribute {
    private Map<String, Object> attributes;
    private String attributeKey;
    private String email;
    private String nickname;

    public static OAuth2Attribute of(String provider, String attributeKey, Map<String, Object> attributes) {
        if (provider.equals("google")) {
            return ofGoogle(attributeKey, attributes);
        }
        throw new IllegalStateException();
    }

    private static OAuth2Attribute ofGoogle(String attributeKey, Map<String, Object> attributes) {
        return OAuth2Attribute.builder()
                .attributes(attributes)
                .attributeKey(attributeKey)
                .email((String) attributes.get("email"))
                .nickname((String) attributes.get("name"))
                .build();
    }

    public Member toEntity() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        String password = passwordEncoder.encode(UUID.randomUUID().toString());

        return Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .roles(roles)
                .build();
    }
}
