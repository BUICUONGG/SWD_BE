package swd.fpt.exegroupingmanagement.config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class FirebaseConfig {

    @Bean
    public FirebaseApp initializeFirebaseApp() throws IOException {
        InputStream serviceAccount = null;
        
        String firebaseKeyJson = System.getenv("FIREBASE_SERVICE_ACCOUNT_JSON");
        if (firebaseKeyJson != null && !firebaseKeyJson.isEmpty()) {
            serviceAccount = new ByteArrayInputStream(
                firebaseKeyJson.getBytes(StandardCharsets.UTF_8)
            );
        } 
        //Load từ classpath (default cho local development)
        else {
                ClassPathResource resource = new ClassPathResource("firebase-service-account.json");
                if (!resource.exists()) {
                    throw new IOException("Firebase service account file not found in classpath.");
                }
                serviceAccount = resource.getInputStream();
        }

        // Tạo FirebaseOptions với credentials
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        // Khởi tạo Firebase App
        FirebaseApp firebaseApp;
        if (FirebaseApp.getApps().isEmpty()) {
            firebaseApp = FirebaseApp.initializeApp(options);
        } else {
            firebaseApp = FirebaseApp.getInstance();
        }

        return firebaseApp;

    }
    /**
     * Bean để gửi FCM messages
     * 
     * @param firebaseApp FirebaseApp instance
     * @return FirebaseMessaging instance
     */
    @Bean
    public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
        return FirebaseMessaging.getInstance(firebaseApp);
    }
}

