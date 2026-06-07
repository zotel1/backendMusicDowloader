package com.principal.backend.infrastructure.adapter.jpa;

import com.principal.backend.domain.model.GoogleAccount;
import com.principal.backend.domain.port.GoogleAccountRepository;
import com.principal.backend.infrastructure.adapter.jpa.entity.GoogleAccountEntity;
import com.principal.backend.infrastructure.adapter.jpa.entity.UserEntity;
import com.principal.backend.infrastructure.adapter.jpa.repository.GoogleAccountJpaRepository;
import com.principal.backend.infrastructure.adapter.jpa.repository.UserJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class JpaGoogleAccountRepositoryAdapter implements GoogleAccountRepository {

    private final GoogleAccountJpaRepository jpaRepository;
    private final UserJpaRepository userJpaRepository;

    public JpaGoogleAccountRepositoryAdapter(GoogleAccountJpaRepository jpaRepository,
                                              UserJpaRepository userJpaRepository) {
        this.jpaRepository = jpaRepository;
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public Optional<GoogleAccount> findByUserId(UUID userId) {
        return jpaRepository.findByUserId(userId).map(GoogleAccountEntity::toDomain);
    }

    @Override
    public void save(GoogleAccount account) {
        UserEntity userEntity = userJpaRepository.findById(account.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + account.getUserId()));
        jpaRepository.save(GoogleAccountEntity.fromDomain(account, userEntity));
    }

    @Override
    public void deleteByUserId(UUID userId) {
        jpaRepository.deleteByUserId(userId);
    }
}
