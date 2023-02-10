package gdsc.skhu.liferary.domain.DTO;

import lombok.Data;

@Data
public class LoginDTO {
    private String email; //id로 받을 email
    private String password;
}
