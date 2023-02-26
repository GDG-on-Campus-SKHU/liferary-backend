package gdsc.skhu.liferary.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class FirebaseService {
    @Value("${firebase.bucket}")
    private String firebaseBucket;

    public String uploadFiles(MultipartFile image) throws IOException {
        Bucket bucket = StorageClient.getInstance().bucket(firebaseBucket);
        InputStream content = new ByteArrayInputStream(image.getBytes());
        Blob blob = bucket.create(image.getOriginalFilename(), content, image.getContentType());
        return blob.getMediaLink();
    }
}
