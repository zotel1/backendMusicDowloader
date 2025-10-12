package com.principal.backend.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.principal.backend.dto.DownloadRequest;
import com.principal.backend.dto.DownloadResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
public class DownloadService {

    public void uploadFileToDrive(String accessToken, String filePath) throws IOException {
        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);

        Drive driveService = new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                credential
        )
                .setApplicationName("Downloader App")
                .build();

        java.io.File fileToUpload = new java.io.File(filePath);
        String mimeType = Files.probeContentType(fileToUpload.toPath());

        com.google.api.services.drive.model.File fileMetadata = new File();
        fileMetadata.setName(fileToUpload.getName());

        FileContent mediaContent = new FileContent(mimeType != null ? mimeType : "application/octet-stream", fileToUpload);

        driveService.files()
                .create(fileMetadata, mediaContent)
                .setFields("id, name")
                .execute();
    }
    }

    /*
    private static final String PYTHON_API_URL = "http://127.0.0.1:8000/api/download/?url=";

    public DownloadResponse downloadFromPython(DownloadRequest request){
        try {
            RestTemplate restTemplate = new RestTemplate();
            String requestUrl = PYTHON_API_URL + request.getUrl();
            ResponseEntity<DownloadResponse> response = restTemplate.getForEntity(requestUrl, DownloadResponse.class);

            return response.getBody();
        } catch (Exception e) {
            return new DownloadResponse("error", e.getMessage(), null);
        }
    }*/
}
