package com.principal.backend.domain.model;

import java.time.Instant;
import java.util.UUID;

public class DownloadJob {

    private final UUID id;
    private final UUID userId;
    private DownloadStatus status;
    private final DownloadType type;
    private int progress;
    private final String sourceUrl;
    private String errorMessage;
    private final Instant createdAt;
    private Instant updatedAt;

    public DownloadJob(UUID id, UUID userId, DownloadStatus status, DownloadType type,
                       int progress, String sourceUrl, String errorMessage,
                       Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.userId = userId;
        this.status = status;
        this.type = type;
        this.progress = progress;
        this.sourceUrl = sourceUrl;
        this.errorMessage = errorMessage;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() { return id; }
    public UUID getUserId() { return userId; }
    public DownloadStatus getStatus() { return status; }
    public DownloadType getType() { return type; }
    public int getProgress() { return progress; }
    public String getSourceUrl() { return sourceUrl; }
    public String getErrorMessage() { return errorMessage; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    public void setStatus(DownloadStatus status) { this.status = status; }
    public void setProgress(int progress) { this.progress = progress; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
