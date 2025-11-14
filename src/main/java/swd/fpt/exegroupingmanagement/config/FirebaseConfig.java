package swd.fpt.exegroupingmanagement.config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
    @ConditionalOnProperty(name = "firebase.enabled", havingValue = "true", matchIfMissing = false)
    public FirebaseApp initializeFirebaseApp() throws IOException {
        log.info("Initializing Firebase App...");
        InputStream serviceAccount = null;
        
        String firebaseKeyJson = System.getenv("FIREBASE_SERVICE_ACCOUNT_JSON");
        if (firebaseKeyJson != null && !firebaseKeyJson.isEmpty()) {
            log.info("Loading Firebase credentials from environment variable");
            serviceAccount = new ByteArrayInputStream(
                firebaseKeyJson.getBytes(StandardCharsets.UTF_8)
            );
        } 
        //Load từ classpath (default cho local development)
        else {
                log.info("Loading Firebase credentials from classpath");
                ClassPathResource resource = new ClassPathResource("firebase-service-account.json");
                if (!resource.exists()) {
                    log.error("Firebase service account file not found in classpath.");
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
            log.info("Firebase App initialized successfully");
        } else {
            firebaseApp = FirebaseApp.getInstance();
            log.info("Using existing Firebase App instance");
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
    @ConditionalOnProperty(name = "firebase.enabled", havingValue = "true", matchIfMissing = false)
    public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
        log.info("Initializing Firebase Messaging");
        return FirebaseMessaging.getInstance(firebaseApp);
    }
}
