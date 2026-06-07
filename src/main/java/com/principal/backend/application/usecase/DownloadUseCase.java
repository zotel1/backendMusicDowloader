package com.principal.backend.application.usecase;

import com.principal.backend.domain.model.DownloadResult;
import com.principal.backend.domain.port.GoogleDriveClient;
import com.principal.backend.domain.port.PythonDownloadClient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DownloadUseCase {

    private final PythonDownloadClient pythonClient;
    private final GoogleDriveClient driveClient;

    public DownloadUseCase(PythonDownloadClient pythonClient, GoogleDriveClient driveClient) {
        this.pythonClient = pythonClient;
        this.driveClient = driveClient;
    }

    public DownloadResult execute(String url, String accessToken) {
        try {
            // 1. Call Python to download the file
            DownloadResult downloadResult = pythonClient.download(url);

            if (downloadResult == null || downloadResult.getFilePath() == null || downloadResult.getFilePath().isEmpty()) {
                return new DownloadResult("error", "Python did not return a valid file path.", null);
            }

            String filePath = downloadResult.getFilePath();

            // 2. Create File and probe MIME type
            File file = new File(filePath);
            String mimeType = probeContentType(filePath);

            // 3. Upload to Google Drive
            driveClient.uploadFile(accessToken, file, mimeType);

            // 4. Delete the temp file
            Files.deleteIfExists(Paths.get(filePath));

            // 5. Return result
            return new DownloadResult("success", "File downloaded and uploaded successfully.", file.getName());

        } catch (Exception e) {
            return new DownloadResult("error", e.getMessage(), null);
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
