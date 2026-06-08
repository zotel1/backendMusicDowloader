package com.principal.backend.application.usecase;

import com.principal.backend.domain.exception.DownloadJobNotFoundException;
import com.principal.backend.domain.model.DownloadJob;
import com.principal.backend.domain.port.DownloadJobRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GetDownloadsUseCase {

    private final DownloadJobRepository downloadJobRepository;

    public GetDownloadsUseCase(DownloadJobRepository downloadJobRepository) {
        this.downloadJobRepository = downloadJobRepository;
    }

    public List<DownloadJob> getUserJobs(UUID userId) {
        return downloadJobRepository.findByUserId(userId);
    }

    public DownloadJob getJobDetail(UUID userId, UUID jobId) {
        DownloadJob job = downloadJobRepository.findById(jobId)
                .orElseThrow(() -> new DownloadJobNotFoundException(jobId));
        if (!job.getUserId().equals(userId)) {
            throw new DownloadJobNotFoundException(jobId);
        }
        return job;
    }
}
