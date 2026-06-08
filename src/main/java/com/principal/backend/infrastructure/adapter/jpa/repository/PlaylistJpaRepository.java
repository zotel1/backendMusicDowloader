package com.principal.backend.infrastructure.adapter.jpa.repository;

import com.principal.backend.infrastructure.adapter.jpa.entity.PlaylistEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PlaylistJpaRepository extends JpaRepository<PlaylistEntity, UUID> {
    List<PlaylistEntity> findByOwnerId(UUID ownerId);
}
