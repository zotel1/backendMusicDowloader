package com.principal.backend.infrastructure.adapter.jpa.repository;

import com.principal.backend.domain.model.DownloadStatus;
import com.principal.backend.infrastructure.adapter.jpa.entity.DownloadJobEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DownloadJobJpaRepository extends JpaRepository<DownloadJobEntity, UUID> {
    List<DownloadJobEntity> findByUserId(UUID userId);
    List<DownloadJobEntity> findByStatus(DownloadStatus status);
}
