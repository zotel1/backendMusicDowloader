package com.principal.backend.infrastructure.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class KafkaConfigTest {

    @Mock
    private KafkaProperties kafkaProperties;

    @Mock
    private KafkaProperties.Consumer consumer;

    private KafkaConfig config;

    @BeforeEach
    void setUp() {
        when(kafkaProperties.getBootstrapServers()).thenReturn(List.of("localhost:9092"));
        when(kafkaProperties.getConsumer()).thenReturn(consumer);
        when(consumer.getGroupId()).thenReturn("test-group");

        config = new KafkaConfig(kafkaProperties);
    }

    @Test
    void createsAllSevenNewTopicBeansWithCorrectNames() {
        NewTopic[] topics = {
                config.downloadRequestedTopic(),
                config.downloadStartedTopic(),
                config.downloadProgressTopic(),
                config.downloadCompletedTopic(),
                config.downloadFailedTopic(),
                config.uploadStartedTopic(),
                config.uploadCompletedTopic()
        };

        String[] expected = {
                "download.requested", "download.started", "download.progress",
                "download.completed", "download.failed",
                "upload.started", "upload.completed"
        };

        assertEquals(7, topics.length);
        for (int i = 0; i < topics.length; i++) {
            assertEquals(expected[i], topics[i].name());
            assertEquals(1, topics[i].numPartitions());
            assertEquals(1, topics[i].replicationFactor());
        }
    }

    @Test
    void createsProducerFactoryJsonSerializer() {
        var factory = config.producerFactory();
        assertNotNull(factory);
    }

    @Test
    void createsKafkaTemplateBean() {
        var template = config.kafkaTemplate();
        assertNotNull(template);
    }

    @Test
    void createsConsumerFactoryJsonDeserializer() {
        var factory = config.consumerFactory();
        assertNotNull(factory);
    }

    @Test
    void createsKafkaListenerContainerFactory() {
        var factory = config.kafkaListenerContainerFactory();
        assertNotNull(factory);
    }

    @Test
    void createsDefaultErrorHandler() {
        var handler = config.errorHandler();
        assertNotNull(handler);
    }
}
