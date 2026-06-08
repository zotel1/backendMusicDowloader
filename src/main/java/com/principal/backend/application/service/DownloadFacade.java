package com.principal.backend.application.service;

import com.principal.backend.application.usecase.DownloadUseCase;
import com.principal.backend.domain.event.DownloadRequestedEvent;
import com.principal.backend.domain.exception.NoLinkedGoogleAccountException;
import com.principal.backend.domain.model.*;
import com.principal.backend.domain.port.DownloadJobRepository;
import com.principal.backend.domain.port.EventPublisher;
import com.principal.backend.domain.port.GoogleAccountRepository;
import com.principal.backend.presentation.dto.DownloadRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class DownloadFacade {

    private final DownloadJobRepository downloadJobRepository;
    private final GoogleAccountRepository googleAccountRepository;
    private final EventPublisher eventPublisher;
    private final DownloadUseCase downloadUseCase;

    public DownloadFacade(DownloadJobRepository downloadJobRepository,
                          GoogleAccountRepository googleAccountRepository,
                          EventPublisher eventPublisher,
                          DownloadUseCase downloadUseCase) {
        this.downloadJobRepository = downloadJobRepository;
        this.googleAccountRepository = googleAccountRepository;
        this.eventPublisher = eventPublisher;
        this.downloadUseCase = downloadUseCase;
    }

    public DownloadJob createDownload(UUID userId, DownloadRequest request) {
        DownloadType type = DownloadType.fromUrl(request.url());

        GoogleAccount account = googleAccountRepository.findByUserId(userId)
                .orElseThrow(NoLinkedGoogleAccountException::new);

        DownloadJob job = new DownloadJob(
                UUID.randomUUID(), userId, DownloadStatus.PENDING,
                type, 0, request.url(), null,
                Instant.now(), Instant.now()
        );
        downloadJobRepository.save(job);

        eventPublisher.publish(DownloadRequestedEvent.create(
                "DownloadFacade", userId, job.getId(),
                request.url(), type.name()
        ));

        return downloadUseCase.execute(userId, request.url());
    }
}
