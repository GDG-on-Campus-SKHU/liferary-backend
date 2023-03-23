package gdsc.skhu.liferary.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class FirebaseService {
    private final Bucket firebaseBucket;

    public String uploadToFirebase(String path, MultipartFile image) throws IOException {
        InputStream content = new ByteArrayInputStream(image.getBytes());
        Blob blob = firebaseBucket.create(path, content, image.getContentType());
        String mediaLink = blob.getMediaLink();
        return "https://firebasestorage.googleapis.com/v0/" +
                mediaLink.substring(mediaLink.lastIndexOf("/b/") + 1);
    }

    public void deleteFromFirebase(String path) {
        if(firebaseBucket.get(path).exists()) {
            firebaseBucket.get(path).delete();
        }
    }
}
