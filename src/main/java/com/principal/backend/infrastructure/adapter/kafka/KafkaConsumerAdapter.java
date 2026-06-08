package com.principal.backend.infrastructure.adapter.kafka;

import com.principal.backend.domain.event.*;
import com.principal.backend.domain.model.DownloadJob;
import com.principal.backend.domain.model.DownloadStatus;
import com.principal.backend.domain.port.DownloadJobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;
import java.util.function.Function;

@Component
public class KafkaConsumerAdapter {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerAdapter.class);

    private final DownloadJobRepository downloadJobRepository;

    public KafkaConsumerAdapter(DownloadJobRepository downloadJobRepository) {
        this.downloadJobRepository = downloadJobRepository;
    }

    @KafkaListener(topics = "download.started", groupId = "${spring.kafka.consumer.group-id}")
    public void handleDownloadStarted(DownloadStartedEvent event) {
        log.info("Received DownloadStartedEvent for job {}", event.downloadJobId());
        updateJobStatus(event.downloadJobId(), DownloadStatus.DOWNLOADING, job -> {
            if (job.getStatus() == DownloadStatus.QUEUED || job.getStatus() == DownloadStatus.PENDING) {
                job.setStatus(DownloadStatus.DOWNLOADING);
                job.setUpdatedAt(Instant.now());
                return true;
            }
            log.debug("Skipping DownloadStartedEvent for job {}: current status is {}", 
                    event.downloadJobId(), job.getStatus());
            return false;
        });
    }

    @KafkaListener(topics = "download.progress", groupId = "${spring.kafka.consumer.group-id}")
    public void handleDownloadProgress(DownloadProgressEvent event) {
        log.info("Received DownloadProgressEvent for job {}: {}%", event.downloadJobId(), event.progress());
        updateJobStatus(event.downloadJobId(), DownloadStatus.DOWNLOADING, job -> {
            if (job.getStatus() == DownloadStatus.DOWNLOADING) {
                job.setProgress(event.progress());
                job.setUpdatedAt(Instant.now());
                return true;
            }
            log.debug("Skipping DownloadProgressEvent for job {}: current status is {}",
                    event.downloadJobId(), job.getStatus());
            return false;
        });
    }

    @KafkaListener(topics = "download.completed", groupId = "${spring.kafka.consumer.group-id}")
    public void handleDownloadCompleted(DownloadCompletedEvent event) {
        log.info("Received DownloadCompletedEvent for job {}", event.downloadJobId());
        updateJobStatus(event.downloadJobId(), DownloadStatus.COMPLETED, job -> {
            if (job.getStatus() != DownloadStatus.COMPLETED && job.getStatus() != DownloadStatus.FAILED) {
                job.setStatus(DownloadStatus.COMPLETED);
                job.setProgress(100);
                job.setUpdatedAt(Instant.now());
                return true;
            }
            log.debug("Skipping DownloadCompletedEvent for job {}: already in terminal state {}",
                    event.downloadJobId(), job.getStatus());
            return false;
        });
    }

    @KafkaListener(topics = "download.failed", groupId = "${spring.kafka.consumer.group-id}")
    public void handleDownloadFailed(DownloadFailedEvent event) {
        log.info("Received DownloadFailedEvent for job {}: {}", event.downloadJobId(), event.reason());
        updateJobStatus(event.downloadJobId(), DownloadStatus.FAILED, job -> {
            if (job.getStatus() != DownloadStatus.COMPLETED && job.getStatus() != DownloadStatus.FAILED) {
                job.setStatus(DownloadStatus.FAILED);
                job.setErrorMessage(event.reason());
                job.setUpdatedAt(Instant.now());
                return true;
            }
            log.debug("Skipping DownloadFailedEvent for job {}: already in terminal state {}",
                    event.downloadJobId(), job.getStatus());
            return false;
        });
    }

    private void updateJobStatus(
            UUID jobId,
            DownloadStatus targetStatus,
            Function<DownloadJob, Boolean> updater) {
        downloadJobRepository.findById(jobId).ifPresentOrElse(job -> {
            boolean updated = updater.apply(job);
            if (updated) {
                downloadJobRepository.save(job);
                log.info("Updated job {} to status {}", jobId, targetStatus);
            }
        }, () -> log.warn("DownloadJob {} not found, skipping event", jobId));
    }
}
