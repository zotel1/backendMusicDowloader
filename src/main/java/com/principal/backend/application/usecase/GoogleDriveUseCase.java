package com.principal.backend.application.usecase;

import com.principal.backend.domain.port.GoogleDriveClient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GoogleDriveUseCase {

    private final GoogleDriveClient driveClient;

    public GoogleDriveUseCase(GoogleDriveClient driveClient) {
        this.driveClient = driveClient;
    }

    public void execute(String accessToken, String filePath) {
        try {
            File file = new File(filePath);
            String mimeType = probeContentType(filePath);
            driveClient.uploadFile(accessToken, file, mimeType);
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to Google Drive: " + e.getMessage(), e);
        }
    }

    private String probeContentType(String filePath) {
        try {
            String mimeType = Files.probeContentType(Paths.get(filePath));
            return mimeType != null ? mimeType : "application/octet-stream";
        } catch (IOException e) {
            return "application/octet-stream";
        }
    }
}
