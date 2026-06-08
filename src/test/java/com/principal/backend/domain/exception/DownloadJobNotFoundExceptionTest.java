package com.principal.backend.domain.exception;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DownloadJobNotFoundExceptionTest {

    @Test
    void shouldContainJobIdInMessage() {
        UUID jobId = UUID.randomUUID();
        DownloadJobNotFoundException exception = new DownloadJobNotFoundException(jobId);

        assertTrue(exception.getMessage().contains(jobId.toString()));
    }

    @Test
    void shouldBeRuntimeException() {
        DownloadJobNotFoundException exception = new DownloadJobNotFoundException(UUID.randomUUID());
        assertInstanceOf(RuntimeException.class, exception);
    }
}
