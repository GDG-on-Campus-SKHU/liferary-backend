package gdsc.skhu.liferary.configure;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {
    @Value ("${firebase.secret}")
    private String secretKeyPath;
    @Bean
    public FirebaseAuth firebaseAuth() throws IOException {
        System.out.println("System.getProperty(\"user.dir\") = " + System.getProperty("user.dir"));
        FileInputStream serviceAccount = new FileInputStream(ResourceUtils
                .getFile(secretKeyPath));
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
        FirebaseApp.initializeApp(options);
        return FirebaseAuth.getInstance(FirebaseApp.getInstance());
    }
}
