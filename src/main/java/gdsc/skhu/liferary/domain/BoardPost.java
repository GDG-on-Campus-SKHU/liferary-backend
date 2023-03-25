package gdsc.skhu.liferary.domain;

import gdsc.skhu.liferary.util.StringListConverter;
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
public class BoardPost extends BaseTime {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_post_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "main_post_id", nullable = false)
    private MainPost mainPost;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member author;

    @Column(name = "context", nullable = false)
    private String context;

    @OneToMany(mappedBy = "boardPost", orphanRemoval = true)
    private List<Comment> comments;

    @Column(name = "images", length = 2048)
    @Convert(converter = StringListConverter.class)
    private List<String> images;
}
