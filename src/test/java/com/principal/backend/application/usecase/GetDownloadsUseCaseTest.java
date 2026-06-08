package com.principal.backend.application.usecase;

import com.principal.backend.application.usecase.GetDownloadsUseCase.JobDetail;
import com.principal.backend.domain.exception.DownloadJobNotFoundException;
import com.principal.backend.domain.model.DownloadJob;
import com.principal.backend.domain.model.MediaFile;
import com.principal.backend.domain.port.DownloadJobRepository;
import com.principal.backend.domain.port.MediaFileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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
    @Mock MediaFileRepository mediaFileRepository;

    @InjectMocks
    GetDownloadsUseCase useCase;

    @Test
    void getUserJobs_ReturnsAllJobsForUser() {
        UUID userId = UUID.randomUUID();
        DownloadJob job1 = mock(DownloadJob.class);
        DownloadJob job2 = mock(DownloadJob.class);
        when(downloadJobRepository.findByUserId(userId)).thenReturn(List.of(job1, job2));

        List<DownloadJob> result = useCase.getUserJobs(userId);

        assertEquals(2, result.size());
    }

    @Test
    void getJobDetail_ReturnsJobDetailWhenFoundAndBelongsToUser() {
        UUID userId = UUID.randomUUID();
        UUID jobId = UUID.randomUUID();
        DownloadJob job = mock(DownloadJob.class);
        MediaFile mediaFile = mock(MediaFile.class);
        when(downloadJobRepository.findById(jobId)).thenReturn(Optional.of(job));
        when(job.getUserId()).thenReturn(userId);
        when(mediaFileRepository.findByDownloadJobId(jobId)).thenReturn(Optional.of(mediaFile));

        JobDetail result = useCase.getJobDetail(userId, jobId);

        assertSame(job, result.job());
        assertSame(mediaFile, result.mediaFile());
    }

    @Test
    void getJobDetail_ThrowsWhenJobBelongsToDifferentUser() {
        UUID userId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();
        UUID jobId = UUID.randomUUID();
        DownloadJob job = mock(DownloadJob.class);
        when(downloadJobRepository.findById(jobId)).thenReturn(Optional.of(job));
        when(job.getUserId()).thenReturn(otherUserId);

        assertThrows(DownloadJobNotFoundException.class,
                () -> useCase.getJobDetail(userId, jobId));
    }

    @Test
    void getJobDetail_ThrowsWhenJobNotFound() {
        UUID userId = UUID.randomUUID();
        UUID jobId = UUID.randomUUID();
        when(downloadJobRepository.findById(jobId)).thenReturn(Optional.empty());

        assertThrows(DownloadJobNotFoundException.class,
                () -> useCase.getJobDetail(userId, jobId));
    }
}
