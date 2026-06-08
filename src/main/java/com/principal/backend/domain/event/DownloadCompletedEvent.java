package com.principal.backend.domain.event;

import java.time.Instant;
import java.util.UUID;

public record DownloadCompletedEvent(
        UUID id,
        Instant timestamp,
        String source,
        UUID downloadJobId,
        UUID mediaFileId
) implements Event {

    public static DownloadCompletedEvent create(
            String source, UUID downloadJobId, UUID mediaFileId
    ) {
        return new DownloadCompletedEvent(
                UUID.randomUUID(), Instant.now(), source, downloadJobId, mediaFileId
        );
    }
}
