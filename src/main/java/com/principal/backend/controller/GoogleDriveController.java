package com.principal.backend.controller;

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
            googleDriveService.uploadFileToDrive(request.getAccessToken(), request.getFilePath());
            return ResponseEntity.ok("Archivo subido correctamente al Drive del usuario.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al subir el archivo: " + e.getMessage());
        }
    }
}
