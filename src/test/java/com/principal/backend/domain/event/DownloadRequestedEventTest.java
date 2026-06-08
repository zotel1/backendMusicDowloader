package com.principal.backend.domain.event;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DownloadRequestedEventTest {

    @Test
    void shouldConstructWithGivenValues() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();
        UUID userId = UUID.randomUUID();
        UUID jobId = UUID.randomUUID();

        DownloadRequestedEvent event = new DownloadRequestedEvent(
                id, now, "test-source", userId, jobId,
                "https://youtube.com/watch?v=dQw4w9WgXcQ", "AUDIO"
        );

        assertEquals(id, event.id());
        assertEquals(now, event.timestamp());
        assertEquals("test-source", event.source());
        assertEquals(userId, event.userId());
        assertEquals(jobId, event.downloadJobId());
        assertEquals("https://youtube.com/watch?v=dQw4w9WgXcQ", event.url());
        assertEquals("AUDIO", event.downloadType());
    }

    @Test
    void shouldCreateWithGeneratedIdAndTimestamp() {
        UUID userId = UUID.randomUUID();
        UUID jobId = UUID.randomUUID();

        DownloadRequestedEvent event = DownloadRequestedEvent.create(
                "test-source", userId, jobId,
                "https://youtube.com/watch?v=dQw4w9WgXcQ", "AUDIO"
        );

        assertNotNull(event.id());
        assertNotNull(event.timestamp());
        assertTrue(event.timestamp().isBefore(Instant.now().plusSeconds(1)));
        assertTrue(event.timestamp().isAfter(Instant.now().minusSeconds(5)));
        assertEquals("test-source", event.source());
        assertEquals(userId, event.userId());
        assertEquals(jobId, event.downloadJobId());
        assertEquals("https://youtube.com/watch?v=dQw4w9WgXcQ", event.url());
        assertEquals("AUDIO", event.downloadType());
    }
}
