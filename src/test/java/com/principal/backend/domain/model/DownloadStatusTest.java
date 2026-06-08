package com.principal.backend.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DownloadStatusTest {

    @Test
    void shouldHaveAllExpectedValues() {
        DownloadStatus[] values = DownloadStatus.values();
        assertEquals(7, values.length);
        assertTrue(contains(values, "PENDING"));
        assertTrue(contains(values, "QUEUED"));
        assertTrue(contains(values, "DOWNLOADING"));
        assertTrue(contains(values, "PROCESSING"));
        assertTrue(contains(values, "UPLOADING"));
        assertTrue(contains(values, "COMPLETED"));
        assertTrue(contains(values, "FAILED"));
    }

    private boolean contains(DownloadStatus[] values, String name) {
        for (DownloadStatus status : values) {
            if (status.name().equals(name)) return true;
        }
        return false;
    }
}
