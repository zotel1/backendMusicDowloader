package com.principal.backend.infrastructure.adapter.jpa.entity;

import com.principal.backend.domain.model.DownloadJob;
import com.principal.backend.domain.model.DownloadStatus;
import com.principal.backend.domain.model.DownloadType;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "download_jobs")
public class DownloadJobEntity {
    @Id private UUID id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id", nullable = false) private UserEntity user;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private DownloadStatus status;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private DownloadType type;
    @Column(nullable = false) private int progress;
    @Column(nullable = false, columnDefinition = "TEXT") private String sourceUrl;
    @Column(columnDefinition = "TEXT") private String errorMessage;
    @Column(name = "created_at", nullable = false) private Instant createdAt;
    @Column(name = "updated_at", nullable = false) private Instant updatedAt;

    public DownloadJobEntity() {}

    public static DownloadJobEntity fromDomain(DownloadJob job, UserEntity userEntity) {
        DownloadJobEntity e = new DownloadJobEntity();
        e.id = job.getId(); e.user = userEntity; e.status = job.getStatus(); e.type = job.getType();
        e.progress = job.getProgress(); e.sourceUrl = job.getSourceUrl(); e.errorMessage = job.getErrorMessage();
        e.createdAt = job.getCreatedAt(); e.updatedAt = job.getUpdatedAt();
        return e;
    }
    public DownloadJob toDomain() {
        return new DownloadJob(id, user.getId(), status, type, progress, sourceUrl, errorMessage, createdAt, updatedAt);
    }

    public UUID getId() { return id; } public void setId(UUID id) { this.id = id; }
    public UserEntity getUser() { return user; } public void setUser(UserEntity user) { this.user = user; }
    public DownloadStatus getStatus() { return status; } public void setStatus(DownloadStatus status) { this.status = status; }
    public DownloadType getType() { return type; } public void setType(DownloadType type) { this.type = type; }
    public int getProgress() { return progress; } public void setProgress(int progress) { this.progress = progress; }
    public String getSourceUrl() { return sourceUrl; } public void setSourceUrl(String sourceUrl) { this.sourceUrl = sourceUrl; }
    public String getErrorMessage() { return errorMessage; } public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public Instant getCreatedAt() { return createdAt; } public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; } public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
