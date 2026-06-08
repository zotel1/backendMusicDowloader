package com.principal.backend.domain.event;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DownloadCompletedEventTest {

    @Test
    void shouldConstructWithGivenValues() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();
        UUID jobId = UUID.randomUUID();
        UUID mediaFileId = UUID.randomUUID();

        DownloadCompletedEvent event = new DownloadCompletedEvent(
                id, now, "test-source", jobId, mediaFileId
        );

        assertEquals(id, event.id());
        assertEquals(now, event.timestamp());
        assertEquals("test-source", event.source());
        assertEquals(jobId, event.downloadJobId());
        assertEquals(mediaFileId, event.mediaFileId());
    }

    @Test
    void shouldCreateWithGeneratedIdAndTimestamp() {
        UUID jobId = UUID.randomUUID();
        UUID mediaFileId = UUID.randomUUID();

        DownloadCompletedEvent event = DownloadCompletedEvent.create(
                "test-source", jobId, mediaFileId
        );

        assertNotNull(event.id());
        assertNotNull(event.timestamp());
        assertTrue(event.timestamp().isBefore(Instant.now().plusSeconds(1)));
        assertTrue(event.timestamp().isAfter(Instant.now().minusSeconds(5)));
        assertEquals("test-source", event.source());
        assertEquals(jobId, event.downloadJobId());
        assertEquals(mediaFileId, event.mediaFileId());
    }
}
