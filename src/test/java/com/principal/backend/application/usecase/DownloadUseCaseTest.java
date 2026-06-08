package com.principal.backend.application.usecase;

import com.principal.backend.application.strategy.DownloadStrategy;
import com.principal.backend.application.strategy.DownloadStrategyFactory;
import com.principal.backend.domain.model.*;
import com.principal.backend.domain.port.DownloadJobRepository;
import com.principal.backend.domain.port.GoogleAccountRepository;
import com.principal.backend.domain.port.MediaFileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
class DownloadUseCaseTest {

    @Mock GoogleAccountRepository googleAccountRepository;
    @Mock DownloadJobRepository downloadJobRepository;
    @Mock DownloadStrategyFactory strategyFactory;
    @Mock MediaFileRepository mediaFileRepository;
    @Mock DownloadStrategy strategy;

    @InjectMocks DownloadUseCase useCase;

    @Test
    void execute_CompletesFullLifecycleAndReturnsCompletedJob() {
        UUID userId = UUID.randomUUID();
        String url = "https://youtube.com/watch?v=test";
        GoogleAccount account = new GoogleAccount(UUID.randomUUID(), userId, "gid",
                "e@m.com", "token", "rt", Instant.now().plusSeconds(3600));
        DownloadResult downloadResult = new DownloadResult("success", "ok", "/tmp/file.mp3");

        when(googleAccountRepository.findByUserId(userId)).thenReturn(Optional.of(account));
        when(strategyFactory.getStrategy(DownloadType.VIDEO)).thenReturn(strategy);
        when(strategy.execute(url, "token")).thenReturn(downloadResult);

        DownloadJob result = useCase.execute(userId, url);

        assertNotNull(result);
        assertEquals(DownloadStatus.COMPLETED, result.getStatus());
        assertEquals(DownloadType.VIDEO, result.getType());
        assertEquals(userId, result.getUserId());
        assertEquals(url, result.getSourceUrl());
        assertNull(result.getErrorMessage());

        verify(downloadJobRepository, times(6)).save(any(DownloadJob.class));
        verify(mediaFileRepository).save(any(MediaFile.class));
    }
}
