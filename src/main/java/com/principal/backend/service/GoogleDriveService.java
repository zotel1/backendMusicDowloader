package com.principal.backend.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.principal.backend.store.InMemoryTokenStore;
import com.principal.backend.store.StoredToken;
import org.springframework.stereotype.Service;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import java.io.IOException;

@Service
public class GoogleDriveService {

    private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private final InMemoryTokenStore tokenStore;

    public GoogleDriveService(InMemoryTokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    private Drive buildDriveWithAccessToken(String accessToken) throws GeneralSecurityException, IOException {
        var transport = GoogleNetHttpTransport.newTrustedTransport();
        var initializer = (com.google.api.client.http.HttpRequestInitializer) request -> {
            request.getHeaders().setAuthorization("Bearer " + accessToken);
        };

        return new Drive.Builder(transport, JSON_FACTORY, initializer)
                .setApplicationName("Downloader App")
                .build();
    }

    public void uploadFileForUser(String userId, java.io.File localFile, String mimeType) throws Exception {
        StoredToken stored = tokenStore.findById(userId);
        if (stored == null) {
            throw new IllegalStateException("No hay tokens guardados para el usuario: " + userId);
        }

        Drive drive = buildDriveWithAccessToken(stored.getAccessToken());

        File fileMetadata = new File();
        fileMetadata.setName(localFile.getName());

        FileContent mediaContent = new FileContent(mimeType, localFile);
        drive.files().create(fileMetadata, mediaContent).setFields("id").execute();
    }

    public void uploadFileWithAccessToken(String accessToken, java.io.File localFile, String mimeType) throws Exception {
        Drive drive = buildDriveWithAccessToken(accessToken);
        File fileMetadata = new File();
        fileMetadata.setName(localFile.getName());
        FileContent mediaContent = new FileContent(mimeType, localFile);
        drive.files().create(fileMetadata, mediaContent).setFields("id").execute();
    }

    /*
    public void uploadFileToDrive(String accessToken, String filePath) throws IOException {
        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);

        Drive driveService = new Drive.Builder(
                credential.getTransport(),
                credential.getJsonFactory(),
                credential
        )
                .setApplicationName("Downloader App")
                .build();

        File fileMetadata = new File();
        fileMetadata.setName("archivo_subido.txt"); //Nombre que aparecera en drive

        java.io.File filePathToUpload = new java.io.File(filePath);
        FileContent mediaContent = new FileContent("text/plain", filePathToUpload);

        driveService.files().create(fileMetadata, mediaContent)
                .setFields("id")
                .execute();
   } */
}
