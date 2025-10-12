package com.principal.backend.controller;

import com.principal.backend.dto.GoogleUploadRequest;
import com.principal.backend.service.GoogleDriveService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/google")
@CrossOrigin(origins = "https://frontend-downloader.vercel.app/descargar")
public class GoogleDriveController {

    private final GoogleDriveService googleDriveService;

    public GoogleDriveController(GoogleDriveService googleDriveService) {
        this.googleDriveService = googleDriveService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadToDrive(@RequestBody GoogleUploadRequest request) {
        try {
            // Ajuste aquí 👇
            java.io.File file = new java.io.File(request.getFilePath());
            String mimeType = java.nio.file.Files.probeContentType(file.toPath());

            googleDriveService.uploadFileWithAccessToken(
                    request.getAccessToken(),
                    file,
                    mimeType
            );

            return ResponseEntity.ok("Archivo subido correctamente al Drive del usuario.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al subir el archivo: " + e.getMessage());
        }
    }
}