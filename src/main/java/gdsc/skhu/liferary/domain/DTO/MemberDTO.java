package gdsc.skhu.liferary.domain.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import gdsc.skhu.liferary.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class MemberDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(name = "MemberDTO.Join")
    public static class Join {

        @NotBlank(message = "이메일을 입력해주세요.")
        //이메일 정규식
        @Pattern(regexp = "^[\\w!#$%&amp;'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&amp;'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$",
                message = "이메일 형식에 맞게 입력해주세요.")
        @Schema(description = "Username(email)", defaultValue = "testuser@gmail.com")
        private String email;

        @NotBlank(message = "닉네임을 입력해주세요.")
        @Size(min=2, message = "닉네임은 2글자 이상 사용 가능합니다.")
        @Schema(description = "Nickname", defaultValue = "LiferaryGood")
        private String nickname;

        @NotBlank(message = "비밀번호를 입력해주세요")
        // 비밀번호 정규식
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,20}$",
                message = "8~20자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
        @Schema(description = "Password", defaultValue = "testpassword")
        private String password;

        @Schema(description = "Password Check", defaultValue = "testpassword")
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

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(name = "MemberDTO.login")
    public static class Login {
        private String email; //id로 받을 email
        private String password;
    }

    @Getter
    @Schema(name = "MemberDTO.Response")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {
        @Schema(description = "id")
        private Long id;

        @Schema(description = "Username(email)")
        private String email;

        @Schema(description = "Nickname")
        private String nickname;

        public Response(Member member) {
            this.id = member.getId();
            this.email = member.getEmail();
            this.nickname = member.getNickname();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "MemberDTO.OAuth2")
    public static class OAuth2 {
        @Schema(description = "Username(email)", defaultValue = "testuser@gmail.com")
        private String email;
        @Schema(description = "Nickname", defaultValue = "LiferaryGood")
        private String nickname;
    }
}

