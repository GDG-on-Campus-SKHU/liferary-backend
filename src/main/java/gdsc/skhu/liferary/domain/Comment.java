package gdsc.skhu.liferary.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseTime {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member writer;

    @ManyToOne
    @JoinColumn(name = "board_post_id", nullable = false)
    private BoardPost boardPost;

    @Column(name = "context", nullable = false)
    private String context;

    @ManyToOne
    @JoinColumn(name = "parent_comment")
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", orphanRemoval = true)
    private List<Comment> childComments;
}
