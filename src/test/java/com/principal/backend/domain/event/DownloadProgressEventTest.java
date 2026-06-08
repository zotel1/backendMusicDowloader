package com.principal.backend.domain.event;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DownloadProgressEventTest {

    @Test
    void shouldConstructWithGivenValues() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();
        UUID jobId = UUID.randomUUID();

        DownloadProgressEvent event = new DownloadProgressEvent(
                id, now, "test-source", jobId, 50
        );

        assertEquals(id, event.id());
        assertEquals(now, event.timestamp());
        assertEquals("test-source", event.source());
        assertEquals(jobId, event.downloadJobId());
        assertEquals(50, event.progress());
    }

    @Test
    void shouldCreateWithGeneratedIdAndTimestamp() {
        UUID jobId = UUID.randomUUID();

        DownloadProgressEvent event = DownloadProgressEvent.create("test-source", jobId, 75);

        assertNotNull(event.id());
        assertNotNull(event.timestamp());
        assertTrue(event.timestamp().isBefore(Instant.now().plusSeconds(1)));
        assertTrue(event.timestamp().isAfter(Instant.now().minusSeconds(5)));
        assertEquals("test-source", event.source());
        assertEquals(jobId, event.downloadJobId());
        assertEquals(75, event.progress());
    }

    @Test
    void shouldAcceptBoundaryProgressValues() {
        UUID jobId = UUID.randomUUID();

        DownloadProgressEvent minEvent = DownloadProgressEvent.create("test-source", jobId, 0);
        DownloadProgressEvent maxEvent = DownloadProgressEvent.create("test-source", jobId, 100);

        assertEquals(0, minEvent.progress());
        assertEquals(100, maxEvent.progress());
    }
}
