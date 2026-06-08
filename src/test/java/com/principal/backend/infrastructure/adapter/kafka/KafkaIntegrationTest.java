package com.principal.backend.infrastructure.adapter.kafka;

import com.principal.backend.domain.event.DownloadCompletedEvent;
import com.principal.backend.domain.event.DownloadRequestedEvent;
import com.principal.backend.domain.event.DownloadStartedEvent;
import com.principal.backend.domain.model.DownloadJob;
import com.principal.backend.domain.model.DownloadStatus;
import com.principal.backend.domain.model.DownloadType;
import com.principal.backend.domain.model.Role;
import com.principal.backend.domain.model.UserStatus;
import com.principal.backend.domain.port.DownloadJobRepository;
import com.principal.backend.infrastructure.adapter.jpa.entity.UserEntity;
import com.principal.backend.infrastructure.adapter.jpa.repository.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Testcontainers
class KafkaIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Container
    static KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.7.0")
    );

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private DownloadJobRepository downloadJobRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    private UUID userId;
    private UUID jobId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        jobId = UUID.randomUUID();

        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setEmail("kafka-test-" + userId + "@example.com");
        user.setPassword("password");
        user.setRole(Role.ROLE_USER);
        user.setStatus(UserStatus.ACTIVE);
        user.setCreatedAt(Instant.now());
        userJpaRepository.save(user);
    }

    @Test
    void shouldPublishAndConsumeDownloadStartedEvent() throws Exception {
        // 1. Create a DownloadJob in PENDING status
        DownloadJob job = new DownloadJob(jobId, userId, DownloadStatus.PENDING, DownloadType.AUDIO,
                0, "https://youtube.com/watch?v=test", null, Instant.now(), Instant.now());
        downloadJobRepository.save(job);

        // 2. Publish DownloadStartedEvent via KafkaTemplate
        DownloadStartedEvent event = DownloadStartedEvent.create("integration-test", jobId);
        kafkaTemplate.send("download.started", event.id().toString(), event)
                .get(10, TimeUnit.SECONDS);

        // 3. Wait for consumer to process
        awaitStatus(jobId, DownloadStatus.DOWNLOADING, 10000);

        // 4. Verify job status is updated
        DownloadJob found = downloadJobRepository.findById(jobId).orElseThrow();
        assertEquals(DownloadStatus.DOWNLOADING, found.getStatus());
    }

    @Test
    void shouldPublishAndConsumeDownloadCompletedEvent() throws Exception {
        // 1. Create a DownloadJob in DOWNLOADING status
        DownloadJob job = new DownloadJob(jobId, userId, DownloadStatus.DOWNLOADING, DownloadType.AUDIO,
                50, "https://youtube.com/watch?v=test", null, Instant.now(), Instant.now());
        downloadJobRepository.save(job);

        // 2. Publish DownloadCompletedEvent via KafkaTemplate
        DownloadCompletedEvent event = DownloadCompletedEvent.create("integration-test", jobId, UUID.randomUUID());
        kafkaTemplate.send("download.completed", event.id().toString(), event)
                .get(10, TimeUnit.SECONDS);

        // 3. Wait for consumer to process
        awaitStatus(jobId, DownloadStatus.COMPLETED, 10000);

        // 4. Verify job status is updated and progress is 100
        DownloadJob found = downloadJobRepository.findById(jobId).orElseThrow();
        assertEquals(DownloadStatus.COMPLETED, found.getStatus());
        assertEquals(100, found.getProgress());
    }

    @Test
    void shouldPublishDownloadRequestedEvent() throws Exception {
        // 1. Create a DownloadJob
        DownloadJob job = new DownloadJob(jobId, userId, DownloadStatus.PENDING, DownloadType.AUDIO,
                0, "https://youtube.com/watch?v=test", null, Instant.now(), Instant.now());
        downloadJobRepository.save(job);

        // 2. Publish DownloadRequestedEvent (producer-only test — no local consumer for this topic)
        DownloadRequestedEvent event = DownloadRequestedEvent.create("integration-test", userId, jobId,
                "https://youtube.com/watch?v=test", "audio");
        kafkaTemplate.send("download.requested", event.id().toString(), event)
                .get(10, TimeUnit.SECONDS);

        // 3. Verify the send completed — event is in the topic for the Python worker (Fase 5)
        //    Job status remains unchanged (no consumer in this service for download.requested)
        DownloadJob found = downloadJobRepository.findById(jobId).orElseThrow();
        assertEquals(DownloadStatus.PENDING, found.getStatus());
    }

    @Test
    void shouldIgnoreDuplicateCompletedEvent() throws Exception {
        // 1. Create a DownloadJob already in COMPLETED status
        DownloadJob job = new DownloadJob(jobId, userId, DownloadStatus.COMPLETED, DownloadType.AUDIO,
                100, "https://youtube.com/watch?v=test", null, Instant.now(), Instant.now());
        downloadJobRepository.save(job);

        // 2. Publish another DownloadCompletedEvent
        DownloadCompletedEvent event = DownloadCompletedEvent.create("integration-test", jobId, UUID.randomUUID());
        kafkaTemplate.send("download.completed", event.id().toString(), event)
                .get(10, TimeUnit.SECONDS);

        // 3. Wait — consumer should skip duplicate
        Thread.sleep(2000);

        // 4. Verify job status remains COMPLETED
        DownloadJob found = downloadJobRepository.findById(jobId).orElseThrow();
        assertEquals(DownloadStatus.COMPLETED, found.getStatus());
        assertEquals(100, found.getProgress());
    }

    private void awaitStatus(UUID id, DownloadStatus expected, long timeoutMs) throws InterruptedException {
        long deadline = System.currentTimeMillis() + timeoutMs;
        while (System.currentTimeMillis() < deadline) {
            Optional<DownloadJob> job = downloadJobRepository.findById(id);
            if (job.isPresent() && job.get().getStatus() == expected) {
                return;
            }
            Thread.sleep(200);
        }
        fail("Timed out waiting for job " + id + " to reach status " + expected);
    }
}
