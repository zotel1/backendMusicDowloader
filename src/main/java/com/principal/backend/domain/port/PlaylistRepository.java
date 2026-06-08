package com.principal.backend.domain.port;

import com.principal.backend.domain.model.Playlist;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlaylistRepository {

    Optional<Playlist> findById(UUID id);

    void save(Playlist playlist);

    List<Playlist> findByOwnerId(UUID ownerId);
}
