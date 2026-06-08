package com.principal.backend.infrastructure.adapter.kafka;

import com.principal.backend.domain.event.*;
import com.principal.backend.domain.model.DownloadJob;
import com.principal.backend.domain.model.DownloadStatus;
import com.principal.backend.domain.model.DownloadType;
import com.principal.backend.domain.port.DownloadJobRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaConsumerAdapterTest {

    @Mock
    private DownloadJobRepository downloadJobRepository;

    private KafkaConsumerAdapter adapter;

    private UUID jobId;

    @BeforeEach
    void setUp() {
        adapter = new KafkaConsumerAdapter(downloadJobRepository);
        jobId = UUID.randomUUID();
    }

    @Test
    void handleDownloadStartedUpdatesStatusToDownloading() {
        var job = createJob(DownloadStatus.QUEUED);
        when(downloadJobRepository.findById(jobId)).thenReturn(Optional.of(job));

        adapter.handleDownloadStarted(DownloadStartedEvent.create("test", jobId));

        verify(downloadJobRepository).save(argThat(j ->
                j.getStatus() == DownloadStatus.DOWNLOADING));
    }

    @Test
    void handleDownloadProgressUpdatesProgress() {
        var job = createJob(DownloadStatus.DOWNLOADING);
        when(downloadJobRepository.findById(jobId)).thenReturn(Optional.of(job));

        adapter.handleDownloadProgress(DownloadProgressEvent.create("test", jobId, 65));

        verify(downloadJobRepository).save(argThat(j ->
                j.getProgress() == 65 && j.getStatus() == DownloadStatus.DOWNLOADING));
    }

    @Test
    void handleDownloadCompletedUpdatesStatusToCompleted() {
        var job = createJob(DownloadStatus.DOWNLOADING);
        when(downloadJobRepository.findById(jobId)).thenReturn(Optional.of(job));

        adapter.handleDownloadCompleted(DownloadCompletedEvent.create("test", jobId, UUID.randomUUID()));

        verify(downloadJobRepository).save(argThat(j ->
                j.getStatus() == DownloadStatus.COMPLETED));
    }

    @Test
    void handleDownloadFailedUpdatesStatusToFailed() {
        var job = createJob(DownloadStatus.DOWNLOADING);
        when(downloadJobRepository.findById(jobId)).thenReturn(Optional.of(job));

        adapter.handleDownloadFailed(DownloadFailedEvent.create("test", jobId, "Connection timeout"));

        verify(downloadJobRepository).save(argThat(j ->
                j.getStatus() == DownloadStatus.FAILED));
    }

    @Test
    void ignoresDownloadCompletedWhenJobAlreadyCompleted() {
        var job = createJob(DownloadStatus.COMPLETED);
        when(downloadJobRepository.findById(jobId)).thenReturn(Optional.of(job));

        adapter.handleDownloadCompleted(DownloadCompletedEvent.create("test", jobId, UUID.randomUUID()));

        verify(downloadJobRepository, never()).save(any());
    }

    @Test
    void ignoresDownloadFailedWhenJobAlreadyFailed() {
        var job = createJob(DownloadStatus.FAILED);
        when(downloadJobRepository.findById(jobId)).thenReturn(Optional.of(job));

        adapter.handleDownloadFailed(DownloadFailedEvent.create("test", jobId, "another error"));

        verify(downloadJobRepository, never()).save(any());
    }

    @Test
    void ignoresDownloadStartedWhenJobAlreadyDownloading() {
        var job = createJob(DownloadStatus.DOWNLOADING);
        when(downloadJobRepository.findById(jobId)).thenReturn(Optional.of(job));

        adapter.handleDownloadStarted(DownloadStartedEvent.create("test", jobId));

        verify(downloadJobRepository, never()).save(any());
    }

    @Test
    void logsWarningWhenJobNotFound() {
        when(downloadJobRepository.findById(jobId)).thenReturn(Optional.empty());

        adapter.handleDownloadStarted(DownloadStartedEvent.create("test", jobId));

        verify(downloadJobRepository, never()).save(any());
    }

    @Test
    void handleDownloadStartedWithQueuedJobUpdatesToDownloading() {
        var job = createJob(DownloadStatus.QUEUED);
        when(downloadJobRepository.findById(jobId)).thenReturn(Optional.of(job));

        adapter.handleDownloadStarted(DownloadStartedEvent.create("test", jobId));

        verify(downloadJobRepository).save(argThat(j ->
                j.getStatus() == DownloadStatus.DOWNLOADING));
    }

    private DownloadJob createJob(DownloadStatus status) {
        return new DownloadJob(jobId, UUID.randomUUID(), status,
                DownloadType.AUDIO, 0, "https://example.com/song",
                null, Instant.now(), Instant.now());
    }
}
