package com.principal.backend.infrastructure.adapter.jpa;

import com.principal.backend.domain.model.User;
import com.principal.backend.domain.port.UserRepository;
import com.principal.backend.infrastructure.adapter.jpa.entity.UserEntity;
import com.principal.backend.infrastructure.adapter.jpa.repository.UserJpaRepository;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.UUID;

@Component
public class JpaUserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository jpaRepository;

    public JpaUserRepositoryAdapter(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(User user) {
        jpaRepository.save(UserEntity.fromDomain(user));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(UserEntity::toDomain);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaRepository.findById(id).map(UserEntity::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }
}
