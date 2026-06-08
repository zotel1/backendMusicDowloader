package com.principal.backend.domain.event;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DownloadFailedEventTest {

    @Test
    void shouldConstructWithGivenValues() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();
        UUID jobId = UUID.randomUUID();

        DownloadFailedEvent event = new DownloadFailedEvent(
                id, now, "test-source", jobId, "Network timeout"
        );

        assertEquals(id, event.id());
        assertEquals(now, event.timestamp());
        assertEquals("test-source", event.source());
        assertEquals(jobId, event.downloadJobId());
        assertEquals("Network timeout", event.reason());
    }

    @Test
    void shouldCreateWithGeneratedIdAndTimestamp() {
        UUID jobId = UUID.randomUUID();

        DownloadFailedEvent event = DownloadFailedEvent.create(
                "test-source", jobId, "File not found"
        );

        assertNotNull(event.id());
        assertNotNull(event.timestamp());
        assertTrue(event.timestamp().isBefore(Instant.now().plusSeconds(1)));
        assertTrue(event.timestamp().isAfter(Instant.now().minusSeconds(5)));
        assertEquals("test-source", event.source());
        assertEquals(jobId, event.downloadJobId());
        assertEquals("File not found", event.reason());
    }

    @Test
    void shouldAcceptEmptyReason() {
        UUID jobId = UUID.randomUUID();

        DownloadFailedEvent event = DownloadFailedEvent.create("test-source", jobId, "");

        assertEquals("", event.reason());
    }
}
