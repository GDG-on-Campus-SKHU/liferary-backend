package gdsc.skhu.liferary.domain;

import gdsc.skhu.liferary.util.StringListConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
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

    @Enumerated(EnumType.STRING)
    private Category category;

    @Size(min = 1, max = 65534)
    @Column(name = "context", columnDefinition = "TEXT", nullable = false)
    private String context;

    @Column(name = "images", length = 2048)
    @Convert(converter = StringListConverter.class)
    private List<String> images;

    @Column(name = "video")
    private String video;
}
