package com.principal.backend.application.strategy;

import com.principal.backend.domain.model.DownloadResult;
import com.principal.backend.domain.model.DownloadType;
import com.principal.backend.domain.port.GoogleDriveClient;
import com.principal.backend.domain.port.PythonDownloadClient;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class VideoDownloadStrategy implements DownloadStrategy {

    private final PythonDownloadClient pythonClient;
    private final GoogleDriveClient driveClient;

    public VideoDownloadStrategy(PythonDownloadClient pythonClient, GoogleDriveClient driveClient) {
        this.pythonClient = pythonClient;
        this.driveClient = driveClient;
    }

    @Override
    public DownloadResult execute(String url, String accessToken) {
        try {
            DownloadResult result = pythonClient.download(url);
            if (result == null || result.getFilePath() == null || result.getFilePath().isEmpty()) {
                return new DownloadResult("error", "No valid file path from download", null);
            }
            File file = new File(result.getFilePath());
            driveClient.uploadFile(accessToken, file, probeContentType(result.getFilePath()));
            Files.deleteIfExists(Paths.get(result.getFilePath()));
            return new DownloadResult("success", "Video downloaded and uploaded successfully", file.getName());
        } catch (Exception e) {
            return new DownloadResult("error", e.getMessage(), null);
        }
    }

    @Override
    public DownloadType getType() {
        return DownloadType.VIDEO;
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
