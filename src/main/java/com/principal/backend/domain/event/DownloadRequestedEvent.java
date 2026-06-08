package com.principal.backend.domain.event;

import java.time.Instant;
import java.util.UUID;

public record DownloadRequestedEvent(
        UUID id,
        Instant timestamp,
        String source,
        UUID userId,
        UUID downloadJobId,
        String url,
        String downloadType
) implements Event {

    public static DownloadRequestedEvent create(
            String source, UUID userId, UUID downloadJobId,
            String url, String downloadType
    ) {
        return new DownloadRequestedEvent(
                UUID.randomUUID(), Instant.now(), source,
                userId, downloadJobId, url, downloadType
        );
    }
}
