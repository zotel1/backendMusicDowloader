package com.principal.backend.domain.event;

import java.time.Instant;
import java.util.UUID;

public record DownloadStartedEvent(
        UUID id,
        Instant timestamp,
        String source,
        UUID downloadJobId
) implements Event {

    public static DownloadStartedEvent create(String source, UUID downloadJobId) {
        return new DownloadStartedEvent(
                UUID.randomUUID(), Instant.now(), source, downloadJobId
        );
    }
}
