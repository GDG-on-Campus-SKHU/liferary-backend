package gdsc.skhu.liferary.domain.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(name = "LoginDTO")
public class LoginDTO {
    @Schema(description = "Title", defaultValue = "testuser@gmail.com")
    private String email; //id로 받을 email
    @Schema(description = "Title", defaultValue = "testpassword")
    private String password;
}
