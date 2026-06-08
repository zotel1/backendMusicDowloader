package com.principal.backend.infrastructure.adapter.kafka;

import com.principal.backend.domain.event.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaProducerAdapterTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    private KafkaProducerAdapter adapter;

    @Captor
    private ArgumentCaptor<String> topicCaptor;

    @Captor
    private ArgumentCaptor<String> keyCaptor;

    @Captor
    private ArgumentCaptor<Object> eventCaptor;

    @BeforeEach
    void setUp() {
        adapter = new KafkaProducerAdapter(kafkaTemplate);
    }

    @Test
    void publishesDownloadRequestedEventToDownloadRequestedTopic() {
        var event = DownloadRequestedEvent.create("test", UUID.randomUUID(), UUID.randomUUID(), "url", "audio");
        when(kafkaTemplate.send(anyString(), anyString(), any()))
                .thenReturn(CompletableFuture.completedFuture(null));

        adapter.publish(event);

        verify(kafkaTemplate).send("download.requested", event.id().toString(), event);
    }

    @Test
    void publishesDownloadStartedEventToDownloadStartedTopic() {
        var event = DownloadStartedEvent.create("test", UUID.randomUUID());
        when(kafkaTemplate.send(anyString(), anyString(), any()))
                .thenReturn(CompletableFuture.completedFuture(null));

        adapter.publish(event);

        verify(kafkaTemplate).send(eq("download.started"), eq(event.id().toString()), eq(event));
    }

    @Test
    void publishesDownloadProgressEventToDownloadProgressTopic() {
        var event = DownloadProgressEvent.create("test", UUID.randomUUID(), 42);
        when(kafkaTemplate.send(anyString(), anyString(), any()))
                .thenReturn(CompletableFuture.completedFuture(null));

        adapter.publish(event);

        verify(kafkaTemplate).send(eq("download.progress"), eq(event.id().toString()), eq(event));
    }

    @Test
    void publishesDownloadCompletedEventToDownloadCompletedTopic() {
        var event = DownloadCompletedEvent.create("test", UUID.randomUUID(), UUID.randomUUID());
        when(kafkaTemplate.send(anyString(), anyString(), any()))
                .thenReturn(CompletableFuture.completedFuture(null));

        adapter.publish(event);

        verify(kafkaTemplate).send(eq("download.completed"), eq(event.id().toString()), eq(event));
    }

    @Test
    void publishesDownloadFailedEventToDownloadFailedTopic() {
        var event = DownloadFailedEvent.create("test", UUID.randomUUID(), "oops");
        when(kafkaTemplate.send(anyString(), anyString(), any()))
                .thenReturn(CompletableFuture.completedFuture(null));

        adapter.publish(event);

        verify(kafkaTemplate).send(eq("download.failed"), eq(event.id().toString()), eq(event));
    }

    @Test
    void doesNotThrowWhenKafkaSendFails() {
        var event = DownloadStartedEvent.create("test", UUID.randomUUID());
        when(kafkaTemplate.send(anyString(), anyString(), any()))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException("Kafka unavailable")));

        assertDoesNotThrow(() -> adapter.publish(event));
        verify(kafkaTemplate).send(anyString(), anyString(), any());
    }

    @Test
    void publishesWithCorrectEventIdAsKey() {
        var event = DownloadStartedEvent.create("test", UUID.randomUUID());
        when(kafkaTemplate.send(anyString(), anyString(), any()))
                .thenReturn(CompletableFuture.completedFuture(null));

        adapter.publish(event);

        verify(kafkaTemplate).send(anyString(), eq(event.id().toString()), any());
    }
}
