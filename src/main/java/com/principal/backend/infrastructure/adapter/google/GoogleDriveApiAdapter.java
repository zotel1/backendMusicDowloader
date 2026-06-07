package com.principal.backend.infrastructure.adapter.google;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.principal.backend.domain.port.GoogleDriveClient;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Component
public class GoogleDriveApiAdapter implements GoogleDriveClient {

    private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    public GoogleDriveApiAdapter() {
    }

    @Override
    public void createFolders(String accessToken) {
        // Will be implemented in Phase 4 (PR 2) with idempotent folder creation
        // for "Music Downloader/Music/" and "Music Downloader/Videos/"
    }

    @Override
    public void uploadFile(String accessToken, java.io.File localFile, String mimeType) {
        try {
            Drive drive = buildDriveWithAccessToken(accessToken);

            File fileMetadata = new File();
            fileMetadata.setName(localFile.getName());

            FileContent mediaContent = new FileContent(mimeType, localFile);
            drive.files().create(fileMetadata, mediaContent)
                    .setFields("id, name")
                    .execute();
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to Google Drive: " + e.getMessage(), e);
        }
    }

    private Drive buildDriveWithAccessToken(String accessToken) throws GeneralSecurityException, IOException {
        var transport = GoogleNetHttpTransport.newTrustedTransport();
        var initializer = (com.google.api.client.http.HttpRequestInitializer) request ->
                request.getHeaders().setAuthorization("Bearer " + accessToken);

        return new Drive.Builder(transport, JSON_FACTORY, initializer)
                .setApplicationName("Downloader App")
                .build();
    }
}
