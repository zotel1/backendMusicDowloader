package com.principal.backend.presentation.controller;

import com.principal.backend.application.usecase.DownloadUseCase;
import com.principal.backend.domain.model.DownloadResult;
import com.principal.backend.presentation.dto.DownloadRequest;
import com.principal.backend.presentation.dto.DownloadResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/download")
public class DownloadController {

    private final DownloadUseCase downloadUseCase;

    public DownloadController(DownloadUseCase downloadUseCase) {
        this.downloadUseCase = downloadUseCase;
    }

    @PostMapping
    public ResponseEntity<DownloadResponse> handleDownload(@RequestBody DownloadRequest request) {
        DownloadResult result = downloadUseCase.execute(request.getUrl(), request.getAccessToken());
        DownloadResponse response = new DownloadResponse(
                result.getStatus(),
                result.getMessage(),
                result.getFileName()
        );
        return ResponseEntity.ok(response);
    }
}
