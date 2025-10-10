package com.principal.backend.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.springframework.stereotype.Service;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;

import java.io.IOException;

@Service
public class GoogleDriveService {

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
   }
}
