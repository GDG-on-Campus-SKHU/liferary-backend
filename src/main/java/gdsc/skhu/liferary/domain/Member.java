package gdsc.skhu.liferary.domain;

import gdsc.skhu.liferary.domain.DTO.MemberDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@Builder
public class Member extends BaseTime{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Column(name = "firebaseAuth", nullable = false)
    private boolean firebaseAuth;

    public static Member ofUser(MemberDTO.Join joinDto) {
        return Member.builder()
                .email(joinDto.getEmail())
                .nickname(joinDto.getNickname())
                .password(joinDto.getPassword())
                .authority(Authority.USER)
                .firebaseAuth(joinDto.isFirebaseAuth())
                .build();
    }

    public static Member ofAdmin(MemberDTO.Join joinDto) {
        return Member.builder()
                .email(joinDto.getEmail())
                .nickname(joinDto.getNickname())
                .password(joinDto.getPassword())
                .authority(Authority.ADMIN)
                .firebaseAuth(joinDto.isFirebaseAuth())
                .build();
    }

    public boolean matchPassword(PasswordEncoder passwordEncoder, String withdrawPassword){
        return passwordEncoder.matches(withdrawPassword, getPassword());
    }

}
