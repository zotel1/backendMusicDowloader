package com.principal.backend.infrastructure.adapter.kafka;

import com.principal.backend.domain.event.*;
import com.principal.backend.domain.port.EventPublisher;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class KafkaProducerAdapter implements EventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaProducerAdapter.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducerAdapter(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostConstruct
    void init() {
        log.info("Kafka producer adapter initialized");
    }

    @Override
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publish(Event event) {
        String topic = resolveTopic(event);
        log.debug("Publishing event {} to topic {}", event.getClass().getSimpleName(), topic);
        kafkaTemplate.send(topic, event.id().toString(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.warn("Failed to publish event {} to topic {}: {}",
                                event.getClass().getSimpleName(), topic, ex.getMessage());
                    }
                });
    }

    private String resolveTopic(Event event) {
        return switch (event) {
            case DownloadRequestedEvent e -> "download.requested";
            case DownloadStartedEvent e -> "download.started";
            case DownloadProgressEvent e -> "download.progress";
            case DownloadCompletedEvent e -> "download.completed";
            case DownloadFailedEvent e -> "download.failed";
        };
    }
}
