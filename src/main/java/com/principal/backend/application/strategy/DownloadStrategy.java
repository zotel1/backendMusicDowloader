package com.principal.backend.application.strategy;

import com.principal.backend.domain.model.DownloadResult;
import com.principal.backend.domain.model.DownloadType;

public interface DownloadStrategy {
    DownloadResult execute(String url, String accessToken);
    DownloadType getType();
}
