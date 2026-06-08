package com.principal.backend.application.usecase;

import com.principal.backend.domain.exception.DownloadJobNotFoundException;
import com.principal.backend.domain.model.DownloadJob;
import com.principal.backend.domain.port.DownloadJobRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetDownloadsUseCaseTest {

    @Mock DownloadJobRepository downloadJobRepository;

    GetDownloadsUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetDownloadsUseCase(downloadJobRepository);
    }

    @Test
    void getUserJobs_ReturnsAllJobsForUser() {
        UUID userId = UUID.randomUUID();
        DownloadJob job1 = mock(DownloadJob.class);
        DownloadJob job2 = mock(DownloadJob.class);
        when(downloadJobRepository.findByUserId(userId)).thenReturn(List.of(job1, job2));

        List<DownloadJob> result = useCase.getUserJobs(userId);

        assertEquals(2, result.size());
        assertSame(job1, result.get(0));
        assertSame(job2, result.get(1));
    }

    @Test
    void getJobDetail_ReturnsJobWhenFoundAndBelongsToUser() {
        UUID userId = UUID.randomUUID();
        UUID jobId = UUID.randomUUID();
        DownloadJob job = mock(DownloadJob.class);
        when(downloadJobRepository.findById(jobId)).thenReturn(Optional.of(job));
        when(job.getUserId()).thenReturn(userId);

        DownloadJob result = useCase.getJobDetail(userId, jobId);

        assertSame(job, result);
    }
}
