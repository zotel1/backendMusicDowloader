package com.principal.backend.infrastructure.adapter.jpa.repository;

import com.principal.backend.infrastructure.adapter.jpa.entity.MediaFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MediaFileJpaRepository extends JpaRepository<MediaFileEntity, UUID> {
    List<MediaFileEntity> findByJobId(UUID jobId);
}
