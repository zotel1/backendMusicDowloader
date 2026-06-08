package com.principal.backend.infrastructure.adapter.jpa.repository;

import com.principal.backend.domain.model.*;
import com.principal.backend.domain.port.*;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class DownloadJpaRepositoryTest {
    @Test void downloadJobRepositoryContract() {
        DownloadJobRepository repo = new DownloadJobRepository() {
            private final Map<UUID, DownloadJob> store = new HashMap<>();
            @Override public Optional<DownloadJob> findById(UUID id) { return Optional.ofNullable(store.get(id)); }
            @Override public List<DownloadJob> findByUserId(UUID userId) {
                return store.values().stream().filter(j -> j.getUserId().equals(userId)).toList();
            }
            @Override public void save(DownloadJob job) { store.put(job.getId(), job); }
            @Override public void deleteById(UUID id) { store.remove(id); }
        };
        UUID id = UUID.randomUUID(), userId = UUID.randomUUID();
        DownloadJob job = new DownloadJob(id, userId, DownloadStatus.PENDING, DownloadType.AUDIO, 0, "url", null, Instant.now(), Instant.now());
        repo.save(job);
        assertTrue(repo.findById(id).isPresent());
        assertEquals(1, repo.findByUserId(userId).size());
        assertEquals(DownloadStatus.PENDING, repo.findByUserId(userId).getFirst().getStatus());
        repo.deleteById(id);
        assertTrue(repo.findById(id).isEmpty());
    }
    @Test void mediaFileRepositoryContract() {
        MediaFileRepository repo = new MediaFileRepository() {
            private final Map<UUID, MediaFile> store = new HashMap<>();
            @Override public Optional<MediaFile> findByDownloadJobId(UUID jobId) {
                return store.values().stream().filter(m -> m.getJobId().equals(jobId)).findFirst();
            }
            @Override public void save(MediaFile mf) { store.put(mf.getId(), mf); }
        };
        UUID id = UUID.randomUUID(), jobId = UUID.randomUUID();
        MediaFile mf = new MediaFile(id, jobId, "T", "A", "Al", "3:00", null, null, null, null, "f123");
        repo.save(mf);
        assertTrue(repo.findByDownloadJobId(jobId).isPresent());
        assertEquals("f123", repo.findByDownloadJobId(jobId).get().getGoogleDriveFileId());
    }
    @Test void playlistRepositoryContract() {
        PlaylistRepository repo = new PlaylistRepository() {
            private final Map<UUID, Playlist> store = new HashMap<>();
            @Override public Optional<Playlist> findById(UUID id) { return Optional.ofNullable(store.get(id)); }
            @Override public void save(Playlist pl) { store.put(pl.getId(), pl); }
            @Override public List<Playlist> findByOwnerId(UUID ownerId) {
                return store.values().stream().filter(p -> p.getOwnerId().equals(ownerId)).toList();
            }
        };
        UUID id = UUID.randomUUID(), ownerId = UUID.randomUUID();
        Playlist pl = new Playlist(id, ownerId, "My Playlist", "https://youtube.com/playlist?list=abc");
        repo.save(pl);
        assertTrue(repo.findById(id).isPresent());
        assertEquals(1, repo.findByOwnerId(ownerId).size());
        assertEquals("My Playlist", repo.findByOwnerId(ownerId).getFirst().getTitle());
    }
}
