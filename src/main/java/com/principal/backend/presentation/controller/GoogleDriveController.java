package com.principal.backend.presentation.controller;

import com.principal.backend.application.usecase.GoogleDriveUseCase;
import com.principal.backend.presentation.dto.GoogleUploadRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/google")
public class GoogleDriveController {

    private final GoogleDriveUseCase googleDriveUseCase;

    public GoogleDriveController(GoogleDriveUseCase googleDriveUseCase) {
        this.googleDriveUseCase = googleDriveUseCase;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadToDrive(@RequestBody GoogleUploadRequest request) {
        googleDriveUseCase.execute(request.getAccessToken(), request.getFilePath());
        return ResponseEntity.ok("Archivo subido correctamente al Drive del usuario.");
    }
}
