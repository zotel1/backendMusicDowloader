package com.principal.backend.presentation.dto;

import com.principal.backend.domain.model.DownloadStatus;
import com.principal.backend.domain.model.DownloadType;
import java.time.Instant;
import java.util.UUID;

public record JobDetailResponse(UUID jobId, DownloadStatus status, DownloadType type,
                                int progress, Instant createdAt, Instant updatedAt,
                                String errorMessage, String sourceUrl,
                                String title, String artist, String album,
                                String duration, String thumbnailUrl, String genre,
                                String channel, String uploadDate, String googleDriveFileId) {}
