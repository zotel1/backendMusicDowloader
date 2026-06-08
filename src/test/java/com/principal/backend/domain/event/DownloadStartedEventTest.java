package com.principal.backend.domain.event;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DownloadStartedEventTest {

    @Test
    void shouldConstructWithGivenValues() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();
        UUID jobId = UUID.randomUUID();

        DownloadStartedEvent event = new DownloadStartedEvent(
                id, now, "test-source", jobId
        );

        assertEquals(id, event.id());
        assertEquals(now, event.timestamp());
        assertEquals("test-source", event.source());
        assertEquals(jobId, event.downloadJobId());
    }

    @Test
    void shouldCreateWithGeneratedIdAndTimestamp() {
        UUID jobId = UUID.randomUUID();

        DownloadStartedEvent event = DownloadStartedEvent.create("test-source", jobId);

        assertNotNull(event.id());
        assertNotNull(event.timestamp());
        assertTrue(event.timestamp().isBefore(Instant.now().plusSeconds(1)));
        assertTrue(event.timestamp().isAfter(Instant.now().minusSeconds(5)));
        assertEquals("test-source", event.source());
        assertEquals(jobId, event.downloadJobId());
    }
}
