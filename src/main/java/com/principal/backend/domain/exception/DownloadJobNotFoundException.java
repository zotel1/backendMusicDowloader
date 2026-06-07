package com.principal.backend.domain.exception;

import java.util.UUID;

public class DownloadJobNotFoundException extends RuntimeException {

    public DownloadJobNotFoundException(UUID jobId) {
        super("Download job not found: " + jobId);
    }
}
