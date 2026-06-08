package com.principal.backend.domain.event;

import java.time.Instant;
import java.util.UUID;

public record DownloadProgressEvent(
        UUID id,
        Instant timestamp,
        String source,
        UUID downloadJobId,
        int progress
) implements Event {

    public static DownloadProgressEvent create(
            String source, UUID downloadJobId, int progress
    ) {
        return new DownloadProgressEvent(
                UUID.randomUUID(), Instant.now(), source, downloadJobId, progress
        );
    }
}
