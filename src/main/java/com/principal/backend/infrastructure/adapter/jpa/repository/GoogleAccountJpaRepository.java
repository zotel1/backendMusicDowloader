package com.principal.backend.infrastructure.adapter.jpa.repository;

import com.principal.backend.infrastructure.adapter.jpa.entity.GoogleAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GoogleAccountJpaRepository extends JpaRepository<GoogleAccountEntity, UUID> {

    Optional<GoogleAccountEntity> findByUserId(UUID userId);

    void deleteByUserId(UUID userId);
}
