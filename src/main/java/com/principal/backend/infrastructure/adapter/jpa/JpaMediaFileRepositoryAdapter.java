package com.principal.backend.infrastructure.adapter.jpa;

import com.principal.backend.domain.model.MediaFile;
import com.principal.backend.domain.port.MediaFileRepository;
import com.principal.backend.infrastructure.adapter.jpa.entity.MediaFileEntity;
import com.principal.backend.infrastructure.adapter.jpa.repository.MediaFileJpaRepository;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.UUID;

@Component
public class JpaMediaFileRepositoryAdapter implements MediaFileRepository {
    private final MediaFileJpaRepository jpaRepository;

    public JpaMediaFileRepositoryAdapter(MediaFileJpaRepository jpaRepository) { this.jpaRepository = jpaRepository; }

    @Override public Optional<MediaFile> findByDownloadJobId(UUID jobId) {
        return jpaRepository.findByJobId(jobId).stream().findFirst().map(MediaFileEntity::toDomain);
    }
    @Override public void save(MediaFile mediaFile) { jpaRepository.save(MediaFileEntity.fromDomain(mediaFile)); }
}
