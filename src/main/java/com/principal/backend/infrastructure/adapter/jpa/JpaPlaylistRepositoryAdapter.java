package com.principal.backend.infrastructure.adapter.jpa;

import com.principal.backend.domain.model.Playlist;
import com.principal.backend.domain.port.PlaylistRepository;
import com.principal.backend.infrastructure.adapter.jpa.entity.PlaylistEntity;
import com.principal.backend.infrastructure.adapter.jpa.repository.PlaylistJpaRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class JpaPlaylistRepositoryAdapter implements PlaylistRepository {
    private final PlaylistJpaRepository jpaRepository;

    public JpaPlaylistRepositoryAdapter(PlaylistJpaRepository jpaRepository) { this.jpaRepository = jpaRepository; }

    @Override public Optional<Playlist> findById(UUID id) { return jpaRepository.findById(id).map(PlaylistEntity::toDomain); }
    @Override public void save(Playlist playlist) { jpaRepository.save(PlaylistEntity.fromDomain(playlist)); }
    @Override public List<Playlist> findByOwnerId(UUID ownerId) {
        return jpaRepository.findByOwnerId(ownerId).stream().map(PlaylistEntity::toDomain).toList();
    }
}
