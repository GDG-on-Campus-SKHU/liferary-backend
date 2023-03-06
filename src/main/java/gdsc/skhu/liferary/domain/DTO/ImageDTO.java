package gdsc.skhu.liferary.domain.DTO;

import gdsc.skhu.liferary.domain.Image;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class ImageDTO {
    @Getter
    @Setter
    @Schema(name = "ImageDTO.Board")
    public static class Response {
        @Schema(description = "BoardPost ID")
        private Long id;
        @Schema(description = "Original image name")
        private String originalImageName;
        @Schema(description = "Stored image name")
        private String storedImageName;
        @Schema(description = "Image file path")
        private String imagePath;
        @Schema(description = "Image file size")
        private Long imageSize;

        public Response(Image image) {
            this.id = image.getId();
            this.originalImageName = image.getOriginalImageName();
            this.storedImageName = image.getStoredImageName();
            this.imagePath = image.getImagePath();
            this.imageSize = image.getImageSize();
        }
    }
}
