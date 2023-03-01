package gdsc.skhu.liferary.repository;

import gdsc.skhu.liferary.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByImagePath(String imagePath);
    Optional<Image> findByStoredImageName(String storedImageName);
}
