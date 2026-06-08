package com.principal.backend.application.strategy;

import com.principal.backend.domain.model.DownloadResult;

@FunctionalInterface
public interface DownloadStrategy {
    DownloadResult execute(String url, String accessToken);
}
