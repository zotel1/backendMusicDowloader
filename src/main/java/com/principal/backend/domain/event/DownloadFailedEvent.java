package com.principal.backend.domain.event;

import java.time.Instant;
import java.util.UUID;

public record DownloadFailedEvent(
        UUID id,
        Instant timestamp,
        String source,
        UUID downloadJobId,
        String reason
) implements Event {

    public static DownloadFailedEvent create(
            String source, UUID downloadJobId, String reason
    ) {
        return new DownloadFailedEvent(
                UUID.randomUUID(), Instant.now(), source, downloadJobId, reason
        );
    }
}
