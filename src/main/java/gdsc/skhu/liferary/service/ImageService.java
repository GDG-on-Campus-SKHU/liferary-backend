package gdsc.skhu.liferary.service;

import gdsc.skhu.liferary.domain.DTO.ImageDTO;
import gdsc.skhu.liferary.domain.Image;
import gdsc.skhu.liferary.domain.MainPost;
import gdsc.skhu.liferary.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final FirebaseService firebaseService;
    private final ImageRepository imageRepository;

    @Transactional
    public ImageDTO.Result uploadImage(String path, List<MultipartFile> files) throws IOException {
        List<String> imagePaths = new ArrayList<>();
        for(MultipartFile file : files) {
            if(file.getContentType() == null || !file.getContentType().startsWith("image/")) {
                throw new IllegalArgumentException("Not supported file format");
            }
            String fileName = UUID.randomUUID().toString();
            String imagePath = firebaseService.uploadToFirebase(path + fileName, file);

            Image image = Image.builder()
                    .originalImageName(file.getOriginalFilename())
                    .storedImageName(fileName)
                    .imagePath(imagePath)
                    .imageSize(file.getSize())
                    .build();

            imageRepository.save(image);
            imagePaths.add(imagePath);
        }
        return new ImageDTO.Result(imagePaths);
    }

    @Transactional
    public ImageDTO.Response findByStoredImageName(String storedImageName) {
        return new ImageDTO.Response(imageRepository.findByStoredImageName(storedImageName)
                .orElseThrow(() -> new NoSuchElementException("Image not found")));
    }

    @Transactional
    public ImageDTO.Response findByImagePath(String imagePath) {
        return new ImageDTO.Response(imageRepository.findByImagePath(imagePath)
                .orElseThrow(() -> new NoSuchElementException("Image not found")));
    }

    @Transactional
    public ResponseEntity<String> deleteImage(String path, String imagePath) {
        Image image = imageRepository.findByImagePath(imagePath)
                .orElseThrow(() -> new NoSuchElementException("Image not found"));
        firebaseService.deleteFromFirebase(path, image.getStoredImageName());
        imageRepository.delete(image);
        return ResponseEntity.ok("Delete success");
    }
}
