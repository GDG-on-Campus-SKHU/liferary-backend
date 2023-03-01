package gdsc.skhu.liferary.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Image extends BaseTime {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @Column(name = "image_original_name", nullable = false)
    private String originalImageName;

    @Column(name = "image_stored_name", nullable = false)
    private String storedImageName;

    @Column(name = "image_path", nullable = false)
    private String imagePath;

    @Column(name = "image_size", nullable = false)
    private Long imageSize;
}
