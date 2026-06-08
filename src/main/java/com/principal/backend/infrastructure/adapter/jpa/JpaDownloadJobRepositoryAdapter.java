package com.principal.backend.infrastructure.adapter.jpa;

import com.principal.backend.domain.model.DownloadJob;
import com.principal.backend.domain.port.DownloadJobRepository;
import com.principal.backend.infrastructure.adapter.jpa.entity.DownloadJobEntity;
import com.principal.backend.infrastructure.adapter.jpa.entity.UserEntity;
import com.principal.backend.infrastructure.adapter.jpa.repository.DownloadJobJpaRepository;
import com.principal.backend.infrastructure.adapter.jpa.repository.UserJpaRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class JpaDownloadJobRepositoryAdapter implements DownloadJobRepository {
    private final DownloadJobJpaRepository jpaRepository;
    private final UserJpaRepository userJpaRepository;

    public JpaDownloadJobRepositoryAdapter(DownloadJobJpaRepository jpaRepository, UserJpaRepository userJpaRepository) {
        this.jpaRepository = jpaRepository;
        this.userJpaRepository = userJpaRepository;
    }

    @Override public Optional<DownloadJob> findById(UUID id) {
        return jpaRepository.findById(id).map(DownloadJobEntity::toDomain);
    }
    @Override public List<DownloadJob> findByUserId(UUID userId) {
        return jpaRepository.findByUserId(userId).stream().map(DownloadJobEntity::toDomain).toList();
    }
    @Override public void save(DownloadJob job) {
        UserEntity userEntity = userJpaRepository.findById(job.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + job.getUserId()));
        jpaRepository.save(DownloadJobEntity.fromDomain(job, userEntity));
    }
    @Override public void deleteById(UUID id) { jpaRepository.deleteById(id); }
}
