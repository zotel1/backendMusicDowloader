package com.principal.backend.domain.event;

import java.time.Instant;
import java.util.UUID;

public sealed interface Event
        permits DownloadRequestedEvent, DownloadStartedEvent,
        DownloadProgressEvent, DownloadCompletedEvent,
        DownloadFailedEvent {

    UUID id();

    Instant timestamp();

    String source();
}
