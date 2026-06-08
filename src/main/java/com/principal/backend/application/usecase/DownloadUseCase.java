package com.principal.backend.application.usecase;

import com.principal.backend.application.strategy.DownloadStrategy;
import com.principal.backend.application.strategy.DownloadStrategyFactory;
import com.principal.backend.domain.exception.NoLinkedGoogleAccountException;
import com.principal.backend.domain.model.*;
import com.principal.backend.domain.port.DownloadJobRepository;
import com.principal.backend.domain.port.GoogleAccountRepository;
import com.principal.backend.domain.port.MediaFileRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class DownloadUseCase {

    private final GoogleAccountRepository googleAccountRepository;
    private final DownloadJobRepository downloadJobRepository;
    private final DownloadStrategyFactory strategyFactory;
    private final MediaFileRepository mediaFileRepository;

    public DownloadUseCase(GoogleAccountRepository googleAccountRepository,
                           DownloadJobRepository downloadJobRepository,
                           DownloadStrategyFactory strategyFactory,
                           MediaFileRepository mediaFileRepository) {
        this.googleAccountRepository = googleAccountRepository;
        this.downloadJobRepository = downloadJobRepository;
        this.strategyFactory = strategyFactory;
        this.mediaFileRepository = mediaFileRepository;
    }

    public DownloadJob execute(UUID userId, String url) {
        DownloadJob job = null;
        try {
            GoogleAccount account = googleAccountRepository.findByUserId(userId)
                    .orElseThrow(NoLinkedGoogleAccountException::new);

            DownloadType type = DownloadType.fromUrl(url);
            job = new DownloadJob(UUID.randomUUID(), userId, DownloadStatus.PENDING,
                    type, 0, url, null, Instant.now(), Instant.now());
            downloadJobRepository.save(job);

            transition(job, DownloadStatus.QUEUED);
            transition(job, DownloadStatus.DOWNLOADING);

            DownloadStrategy strategy = strategyFactory.getStrategy(type);
            DownloadResult result = strategy.execute(url, account.getAccessToken());

            transition(job, DownloadStatus.PROCESSING);
            transition(job, DownloadStatus.UPLOADING);

            MediaFile mediaFile = new MediaFile(UUID.randomUUID(), job.getId(),
                    result != null ? result.getFileName() : null, null, null,
                    null, null, null, null, null, null);
            mediaFileRepository.save(mediaFile);

            transition(job, DownloadStatus.COMPLETED);
        } catch (Exception e) {
            if (job != null) {
                job.setStatus(DownloadStatus.FAILED);
                job.setErrorMessage(e.getMessage());
                job.setUpdatedAt(Instant.now());
                downloadJobRepository.save(job);
            }
        }
        return job;
    }

    private void transition(DownloadJob job, DownloadStatus status) {
        job.setStatus(status);
        job.setUpdatedAt(Instant.now());
        downloadJobRepository.save(job);
    }
}
