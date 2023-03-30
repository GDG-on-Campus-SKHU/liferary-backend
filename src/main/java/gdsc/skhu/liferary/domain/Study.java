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
public class Study extends BaseTime {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member author;

    @OneToOne
    @JoinColumn(name = "main_post_id", nullable = false, unique = true)
    private MainPost mainPost;

    @Column(name = "title", nullable = false)
    private String title;

    @Size(min = 1, max = 65534)
    @Column(name = "context", columnDefinition = "TEXT", nullable = false)
    private String context;

    @Column(name = "images", length = 2048)
    @Convert(converter = StringListConverter.class)
    private List<String> images;
}
