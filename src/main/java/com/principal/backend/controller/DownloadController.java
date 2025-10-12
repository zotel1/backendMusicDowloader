package com.principal.backend.controller;

import com.principal.backend.dto.DownloadRequest;
import com.principal.backend.dto.DownloadResponse;
import com.principal.backend.service.DownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/download")
@CrossOrigin(origins = "https://frontend-downloader.vercel.app")
public class DownloadController {

    @Autowired
    private DownloadService downloadService;

    @PostMapping
    public ResponseEntity<DownloadResponse> handleDownload(@RequestBody DownloadRequest request) {
        try {
            DownloadResponse response = downloadService.handleDownloadProcess(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new DownloadResponse("error", e.getMessage(), null));
        }
    }
}
