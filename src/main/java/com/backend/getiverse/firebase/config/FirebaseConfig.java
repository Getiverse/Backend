package com.backend.getiverse.firebase.config;

import com.backend.getiverse.firebase.auth.models.SecurityProperties;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Autowired
    SecurityProperties secProps;

    @Primary
    @Bean
    public void firebaseInit() {
        InputStream inputStream = null;
        try {
            inputStream = new ClassPathResource("getiverse-backend-firebase-adminsdk-5pkmu-74ee66e64c.json").getInputStream();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(inputStream))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
            System.out.println("Firebase Initialize");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
