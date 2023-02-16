package gdsc.skhu.liferary.domain.DTO;

import gdsc.skhu.liferary.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
public class SignUpDTO {

    @NotBlank(message = "이메일을 입력해주세요.")
    //이메일 정규식
    @Pattern(regexp = "^[\\w!#$%&amp;'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&amp;'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$"
            ,message = "이메일 형식에 맞게 입력해주세요.")
    private String email;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min=2, message = "닉네임은 2글자 이상 사용 가능합니다.")
    private String nickname;

    @NotBlank(message = "비밀번호를 입력해주세요")
    // 비밀번호 정규식
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,20}$",
            message = "8~20자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;

    // 비밀번호 일치 확인
    private String checkedPassword;


    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();
    }
}
