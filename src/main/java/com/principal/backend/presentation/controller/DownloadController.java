package com.principal.backend.presentation.controller;

import com.principal.backend.application.service.DownloadFacade;
import com.principal.backend.application.usecase.GetDownloadsUseCase;
import com.principal.backend.application.usecase.GetDownloadsUseCase.JobDetail;
import com.principal.backend.domain.model.DownloadJob;
import com.principal.backend.domain.model.MediaFile;
import com.principal.backend.domain.model.User;
import com.principal.backend.presentation.dto.DownloadRequest;
import com.principal.backend.presentation.dto.DownloadResponse;
import com.principal.backend.presentation.dto.JobDetailResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/download")
public class DownloadController {

    private final DownloadFacade downloadFacade;
    private final GetDownloadsUseCase getDownloadsUseCase;

    public DownloadController(DownloadFacade downloadFacade,
                              GetDownloadsUseCase getDownloadsUseCase) {
        this.downloadFacade = downloadFacade;
        this.getDownloadsUseCase = getDownloadsUseCase;
    }

    @PostMapping
    public ResponseEntity<DownloadResponse> handleDownload(@RequestBody DownloadRequest request) {
        UUID userId = getAuthenticatedUserId();
        DownloadJob job = downloadFacade.createDownload(userId, request);
        return ResponseEntity.ok(toResponse(job));
    }

    @GetMapping
    public ResponseEntity<List<DownloadResponse>> listDownloads() {
        UUID userId = getAuthenticatedUserId();
        List<DownloadJob> jobs = getDownloadsUseCase.getUserJobs(userId);
        List<DownloadResponse> responses = jobs.stream().map(this::toResponse).toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobDetailResponse> getDownloadDetail(@PathVariable UUID id) {
        UUID userId = getAuthenticatedUserId();
        JobDetail detail = getDownloadsUseCase.getJobDetail(userId, id);
        return ResponseEntity.ok(toDetailResponse(detail.job(), detail.mediaFile()));
    }

    private UUID getAuthenticatedUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || !(auth.getPrincipal() instanceof User user)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }
        return user.getId();
    }

    private DownloadResponse toResponse(DownloadJob job) {
        return new DownloadResponse(job.getId(), job.getStatus(), job.getType(),
                job.getProgress(), job.getCreatedAt(), job.getUpdatedAt(),
                job.getErrorMessage());
    }

    private JobDetailResponse toDetailResponse(DownloadJob job, MediaFile media) {
        return new JobDetailResponse(
                job.getId(), job.getStatus(), job.getType(),
                job.getProgress(), job.getCreatedAt(), job.getUpdatedAt(),
                job.getErrorMessage(), job.getSourceUrl(),
                media != null ? media.getTitle() : null,
                media != null ? media.getArtist() : null,
                media != null ? media.getAlbum() : null,
                media != null ? media.getDuration() : null,
                media != null ? media.getThumbnailUrl() : null,
                media != null ? media.getGenre() : null,
                media != null ? media.getChannel() : null,
                media != null ? media.getUploadDate() : null,
                media != null ? media.getGoogleDriveFileId() : null);
    }
}
