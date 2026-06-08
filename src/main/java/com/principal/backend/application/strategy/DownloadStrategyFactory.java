package com.principal.backend.application.strategy;

import com.principal.backend.domain.model.DownloadType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class DownloadStrategyFactory {

    private final Map<DownloadType, DownloadStrategy> strategyMap;

    public DownloadStrategyFactory(List<DownloadStrategy> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(DownloadStrategy::getType, Function.identity()));
    }

    public DownloadStrategy getStrategy(DownloadType type) {
        DownloadStrategy strategy = strategyMap.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("No strategy found for type: " + type);
        }
        return strategy;
    }
}
