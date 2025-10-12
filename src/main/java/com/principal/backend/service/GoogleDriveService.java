package com.principal.backend.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.security.GeneralSecurityException;

@Service
public class GoogleDriveService {

    private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private Drive buildDriveWithAccessToken(String accessToken) throws GeneralSecurityException, IOException {
        var transport = GoogleNetHttpTransport.newTrustedTransport();
        var initializer = (com.google.api.client.http.HttpRequestInitializer) request ->
                request.getHeaders().setAuthorization("Bearer " + accessToken);

        return new Drive.Builder(transport, JSON_FACTORY, initializer)
                .setApplicationName("Downloader App")
                .build();
    }

    public void uploadFileWithAccessToken(String accessToken, java.io.File localFile, String mimeType) throws Exception {
        Drive drive = buildDriveWithAccessToken(accessToken);

        File fileMetadata = new File();
        fileMetadata.setName(localFile.getName());

        FileContent mediaContent = new FileContent(mimeType, localFile);
        drive.files().create(fileMetadata, mediaContent)
                .setFields("id, name")
                .execute();
    }
}