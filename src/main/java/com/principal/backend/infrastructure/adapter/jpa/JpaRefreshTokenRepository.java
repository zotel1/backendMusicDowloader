package com.principal.backend.infrastructure.adapter.jpa;

import com.principal.backend.infrastructure.adapter.jpa.entity.RefreshTokenEntity;
import com.principal.backend.infrastructure.adapter.jpa.repository.RefreshTokenJpaRepository;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Component
public class JpaRefreshTokenRepository {

    private final RefreshTokenJpaRepository jpaRepository;

    public JpaRefreshTokenRepository(RefreshTokenJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    public void save(UUID userId, String token, Instant expiresAt) {
        RefreshTokenEntity entity = new RefreshTokenEntity(
            UUID.randomUUID(), userId, token, expiresAt, Instant.now()
        );
        jpaRepository.save(entity);
    }

    public Optional<RefreshTokenEntity> findByToken(String token) {
        return jpaRepository.findByToken(token);
    }

    public void deleteByUserId(UUID userId) {
        // Will be implemented when needed
    }
}
