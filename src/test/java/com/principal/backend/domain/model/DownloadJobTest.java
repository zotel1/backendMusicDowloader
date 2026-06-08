package com.principal.backend.domain.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DownloadJobTest {

    @Test
    void shouldCreateDownloadJobWithAllFields() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Instant now = Instant.now();

        DownloadJob job = new DownloadJob(
                id, userId, DownloadStatus.PENDING, DownloadType.AUDIO,
                0, "https://youtube.com/watch?v=dQw4w9WgXcQ", null, now, now
        );

        assertEquals(id, job.getId());
        assertEquals(userId, job.getUserId());
        assertEquals(DownloadStatus.PENDING, job.getStatus());
        assertEquals(DownloadType.AUDIO, job.getType());
        assertEquals(0, job.getProgress());
        assertEquals("https://youtube.com/watch?v=dQw4w9WgXcQ", job.getSourceUrl());
        assertNull(job.getErrorMessage());
        assertEquals(now, job.getCreatedAt());
        assertEquals(now, job.getUpdatedAt());
    }

    @Test
    void shouldUpdateMutableFields() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Instant now = Instant.now();
        Instant later = now.plusSeconds(60);

        DownloadJob job = new DownloadJob(
                id, userId, DownloadStatus.PENDING, DownloadType.VIDEO,
                0, "https://youtu.be/dQw4w9WgXcQ", null, now, now
        );

        job.setStatus(DownloadStatus.DOWNLOADING);
        job.setProgress(50);
        job.setErrorMessage("Something went wrong");
        job.setUpdatedAt(later);

        assertEquals(DownloadStatus.DOWNLOADING, job.getStatus());
        assertEquals(50, job.getProgress());
        assertEquals("Something went wrong", job.getErrorMessage());
        assertEquals(later, job.getUpdatedAt());

        // Immutable fields should not change
        assertEquals(id, job.getId());
        assertEquals(userId, job.getUserId());
        assertEquals(DownloadType.VIDEO, job.getType());
        assertEquals(now, job.getCreatedAt());
    }
}
