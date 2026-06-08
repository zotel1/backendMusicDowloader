package com.principal.backend.domain.port;

import com.principal.backend.domain.model.DownloadJob;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DownloadJobRepository {

    Optional<DownloadJob> findById(UUID id);

    List<DownloadJob> findByUserId(UUID userId);

    void save(DownloadJob job);

    void deleteById(UUID id);
}
