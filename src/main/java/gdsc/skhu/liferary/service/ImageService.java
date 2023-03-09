package gdsc.skhu.liferary.service;

import gdsc.skhu.liferary.domain.DTO.ImageDTO;
import gdsc.skhu.liferary.domain.Image;
import gdsc.skhu.liferary.domain.MainPost;
import gdsc.skhu.liferary.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final FirebaseService firebaseService;
    private final ImageRepository imageRepository;

    @Transactional
    public ImageDTO.Response uploadImage(String path, MultipartFile file) throws IOException {
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

        return new ImageDTO.Response(imageRepository.save(image));
    }

    @Transactional
    public ImageDTO.Response findByStoredImageName(String storedImageName) {
        return new ImageDTO.Response(imageRepository.findByStoredImageName(storedImageName)
                .orElseThrow(() -> new NoSuchElementException("Image not found")));
    }

    @Transactional
    public void deleteImage(String imagePath) {
        Image image = imageRepository.findByImagePath(imagePath)
                .orElseThrow(() -> new NoSuchElementException("Image not found"));
        firebaseService.deleteFromFirebase(imagePath);
        imageRepository.delete(image);
    }
}
