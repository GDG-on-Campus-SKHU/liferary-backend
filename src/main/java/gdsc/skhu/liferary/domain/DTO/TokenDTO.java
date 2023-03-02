package gdsc.skhu.liferary.domain.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDTO {
    private String grantType;
    private String accessToken;
    private String refreshToken;

    public static TokenDTO of(String accessToken, String refreshToken) {
        return TokenDTO.builder()
                .grantType("bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}