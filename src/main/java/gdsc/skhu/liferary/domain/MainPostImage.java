package gdsc.skhu.liferary.domain;

import javax.persistence.*;

@Entity
public class MainPostImage extends BaseTime {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "main_post_id", nullable = false)
    private MainPost mainPost;

    @Column(name = "image_original_name", nullable = false)
    private String originalImageName;

    @Column(name = "image_stored_name", nullable = false)
    private String storedImageName;

    @Column(name = "image_path", nullable = false)
    private String imagePath;

    @Column(name = "image_size", nullable = false)
    private Long imageSize;
}
