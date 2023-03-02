package gdsc.skhu.liferary.domain;

import gdsc.skhu.liferary.domain.DTO.MemberDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@Builder
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    public static Member ofUser(MemberDTO.Join joinDto) {
        return Member.builder()
                .email(joinDto.getEmail())
                .nickname(joinDto.getNickname())
                .password(joinDto.getPassword())
                .authority(Authority.USER)
                .build();
    }

    public static Member ofAdmin(MemberDTO.Join joinDto) {
        return Member.builder()
                .email(joinDto.getEmail())
                .nickname(joinDto.getNickname())
                .password(joinDto.getPassword())
                .authority(Authority.ADMIN)
                .build();
    }

}
