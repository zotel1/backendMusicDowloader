package com.principal.backend.application.service;

import com.principal.backend.application.usecase.DownloadUseCase;
import com.principal.backend.domain.event.DownloadRequestedEvent;
import com.principal.backend.domain.exception.NoLinkedGoogleAccountException;
import com.principal.backend.domain.model.*;
import com.principal.backend.domain.port.DownloadJobRepository;
import com.principal.backend.domain.port.EventPublisher;
import com.principal.backend.domain.port.GoogleAccountRepository;
import com.principal.backend.presentation.dto.DownloadRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DownloadFacadeTest {

    @Mock
    private DownloadJobRepository downloadJobRepository;

    @Mock
    private GoogleAccountRepository googleAccountRepository;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private DownloadUseCase downloadUseCase;

    @InjectMocks
    private DownloadFacade downloadFacade;

    private static final UUID USER_ID = UUID.randomUUID();
    private static final String URL = "https://youtube.com/watch?v=test123";
    private static final DownloadRequest REQUEST = new DownloadRequest(URL);

    @Test
    void createDownload_WithValidRequest_ReturnsCompletedJob() {
        GoogleAccount account = new GoogleAccount(
                UUID.randomUUID(), USER_ID, "gid", "e@m.com",
                "token", "rt", Instant.now().plusSeconds(3600));
        DownloadJob expectedJob = new DownloadJob(
                UUID.randomUUID(), USER_ID, DownloadStatus.COMPLETED,
                DownloadType.VIDEO, 100, URL, null,
                Instant.now(), Instant.now());

        when(googleAccountRepository.findByUserId(USER_ID)).thenReturn(Optional.of(account));
        when(downloadUseCase.execute(USER_ID, URL)).thenReturn(expectedJob);

        DownloadJob result = downloadFacade.createDownload(USER_ID, REQUEST);

        assertNotNull(result);
        assertEquals(DownloadStatus.COMPLETED, result.getStatus());
        assertEquals(DownloadType.VIDEO, result.getType());
        assertEquals(USER_ID, result.getUserId());
        assertEquals(URL, result.getSourceUrl());

        verify(downloadJobRepository).save(any(DownloadJob.class));
        verify(eventPublisher).publish(any(DownloadRequestedEvent.class));
        verify(downloadUseCase).execute(USER_ID, URL);
    }

    @Test
    void createDownload_WithNoLinkedAccount_ThrowsNoLinkedGoogleAccountException() {
        when(googleAccountRepository.findByUserId(USER_ID)).thenReturn(Optional.empty());

        assertThrows(NoLinkedGoogleAccountException.class,
                () -> downloadFacade.createDownload(USER_ID, REQUEST));

        verify(downloadJobRepository, never()).save(any());
        verify(eventPublisher, never()).publish(any());
        verify(downloadUseCase, never()).execute(any(), any());
    }

    @Test
    void createDownload_PublishesEventAfterSavingJob() {
        GoogleAccount account = new GoogleAccount(
                UUID.randomUUID(), USER_ID, "gid", "e@m.com",
                "token", "rt", Instant.now().plusSeconds(3600));

        when(googleAccountRepository.findByUserId(USER_ID)).thenReturn(Optional.of(account));
        when(downloadUseCase.execute(USER_ID, URL)).thenReturn(mock(DownloadJob.class));

        downloadFacade.createDownload(USER_ID, REQUEST);

        InOrder inOrder = inOrder(downloadJobRepository, eventPublisher);
        inOrder.verify(downloadJobRepository).save(any(DownloadJob.class));
        inOrder.verify(eventPublisher).publish(any(DownloadRequestedEvent.class));
    }

    @Test
    void createDownload_CallsUseCaseWithCorrectParameters() {
        GoogleAccount account = new GoogleAccount(
                UUID.randomUUID(), USER_ID, "gid", "e@m.com",
                "token", "rt", Instant.now().plusSeconds(3600));

        when(googleAccountRepository.findByUserId(USER_ID)).thenReturn(Optional.of(account));
        when(downloadUseCase.execute(USER_ID, URL)).thenReturn(mock(DownloadJob.class));

        downloadFacade.createDownload(USER_ID, REQUEST);

        verify(downloadUseCase).execute(USER_ID, URL);
        verify(downloadUseCase, never()).execute(any(), eq("wrong-url"));
    }
}
