package gdsc.skhu.liferary.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Comment extends BaseTime {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member writer;

    @ManyToOne
    @JoinColumn(name = "board_post_id", nullable = false)
    private BoardPost boardPost;

    @Column(name = "context")
    private String context;
}
