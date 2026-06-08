package com.principal.backend.presentation.controller;

import com.principal.backend.application.usecase.DownloadUseCase;
import com.principal.backend.domain.model.DownloadJob;
import com.principal.backend.domain.model.User;
import com.principal.backend.presentation.dto.DownloadRequest;
import com.principal.backend.presentation.dto.DownloadResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/download")
public class DownloadController {

    private final DownloadUseCase downloadUseCase;

    public DownloadController(DownloadUseCase downloadUseCase) {
        this.downloadUseCase = downloadUseCase;
    }

    @PostMapping
    public ResponseEntity<DownloadResponse> handleDownload(@RequestBody DownloadRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || !(auth.getPrincipal() instanceof User user)) {
            return ResponseEntity.status(401).body(new DownloadResponse("error", "Authentication required", null));
        }

        UUID userId = user.getId();
        DownloadJob job = downloadUseCase.execute(userId, request.getUrl());

        DownloadResponse response = new DownloadResponse(
                job.getStatus().name(),
                job.getErrorMessage(),
                job.getSourceUrl()
        );
        return ResponseEntity.ok(response);
    }
}
