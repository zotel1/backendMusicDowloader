package com.principal.backend.application.strategy;

import com.principal.backend.domain.model.DownloadType;
import org.springframework.stereotype.Component;

@Component
public class DownloadStrategyFactory {

    public DownloadStrategy getStrategy(DownloadType type) {
        throw new UnsupportedOperationException("Strategies not yet implemented for " + type);
    }
}
