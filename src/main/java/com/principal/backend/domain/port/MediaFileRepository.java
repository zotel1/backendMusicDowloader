package com.principal.backend.domain.port;

import com.principal.backend.domain.model.MediaFile;

import java.util.Optional;
import java.util.UUID;

public interface MediaFileRepository {

    Optional<MediaFile> findByDownloadJobId(UUID jobId);

    void save(MediaFile mediaFile);
}
