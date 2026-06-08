package com.principal.backend.application.usecase;

import com.principal.backend.domain.exception.DownloadJobNotFoundException;
import com.principal.backend.domain.model.DownloadJob;
import com.principal.backend.domain.model.MediaFile;
import com.principal.backend.domain.port.DownloadJobRepository;
import com.principal.backend.domain.port.MediaFileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GetDownloadsUseCase {

    private final DownloadJobRepository downloadJobRepository;
    private final MediaFileRepository mediaFileRepository;

    public GetDownloadsUseCase(DownloadJobRepository downloadJobRepository,
                               MediaFileRepository mediaFileRepository) {
        this.downloadJobRepository = downloadJobRepository;
        this.mediaFileRepository = mediaFileRepository;
    }

    public List<DownloadJob> getUserJobs(UUID userId) {
        return downloadJobRepository.findByUserId(userId);
    }

    public JobDetail getJobDetail(UUID userId, UUID jobId) {
        DownloadJob job = downloadJobRepository.findById(jobId)
                .orElseThrow(() -> new DownloadJobNotFoundException(jobId));
        if (!job.getUserId().equals(userId)) {
            throw new DownloadJobNotFoundException(jobId);
        }
        MediaFile mediaFile = mediaFileRepository.findByDownloadJobId(jobId).orElse(null);
        return new JobDetail(job, mediaFile);
    }

    public record JobDetail(DownloadJob job, MediaFile mediaFile) {}
}
