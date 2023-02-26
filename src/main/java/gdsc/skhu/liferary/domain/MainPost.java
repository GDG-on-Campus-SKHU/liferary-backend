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
public class MainPost extends BaseTime {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "main_post_id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "author", nullable = false)
    private Member author;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "context", nullable = false)
    private String context;

    @OneToMany(mappedBy = "mainPost", orphanRemoval = true)
    private List<MainPostImage> images;

    @Column(name = "video")
    private String video;
}
