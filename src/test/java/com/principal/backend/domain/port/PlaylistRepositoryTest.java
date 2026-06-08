package com.principal.backend.domain.port;

import com.principal.backend.domain.model.Playlist;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PlaylistRepositoryTest {

    @Test
    void shouldFindById() {
        PlaylistRepository repo = new PlaylistRepository() {
            @Override
            public Optional<Playlist> findById(UUID id) {
                return Optional.empty();
            }

            @Override
            public void save(Playlist playlist) {}

            @Override
            public List<Playlist> findByOwnerId(UUID ownerId) {
                return List.of();
            }
        };

        Optional<Playlist> result = repo.findById(UUID.randomUUID());
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldSupportSaveAndFindById() {
        PlaylistRepository repo = new PlaylistRepository() {
            private Playlist stored;

            @Override
            public Optional<Playlist> findById(UUID id) {
                return stored != null && stored.getId().equals(id)
                        ? Optional.of(stored) : Optional.empty();
            }

            @Override
            public void save(Playlist playlist) {
                this.stored = playlist;
            }

            @Override
            public List<Playlist> findByOwnerId(UUID ownerId) {
                return stored != null && stored.getOwnerId().equals(ownerId)
                        ? List.of(stored) : List.of();
            }
        };

        UUID id = UUID.randomUUID();
        Playlist playlist = new Playlist(id, UUID.randomUUID(), "My List",
                "https://youtube.com/playlist?list=PLabc123");

        repo.save(playlist);
        assertTrue(repo.findById(id).isPresent());
        assertEquals("My List", repo.findById(id).get().getTitle());
    }

    @Test
    void shouldFindByOwnerId() {
        PlaylistRepository repo = new PlaylistRepository() {
            private Playlist stored;

            @Override
            public Optional<Playlist> findById(UUID id) {
                return stored != null && stored.getId().equals(id)
                        ? Optional.of(stored) : Optional.empty();
            }

            @Override
            public void save(Playlist playlist) {
                this.stored = playlist;
            }

            @Override
            public List<Playlist> findByOwnerId(UUID ownerId) {
                return stored != null && stored.getOwnerId().equals(ownerId)
                        ? List.of(stored) : List.of();
            }
        };

        UUID ownerId = UUID.randomUUID();
        Playlist playlist = new Playlist(UUID.randomUUID(), ownerId, "Favorites",
                "https://youtube.com/playlist?list=PLxyz");
        repo.save(playlist);

        List<Playlist> results = repo.findByOwnerId(ownerId);
        assertEquals(1, results.size());
        assertEquals(ownerId, results.get(0).getOwnerId());
    }
}
