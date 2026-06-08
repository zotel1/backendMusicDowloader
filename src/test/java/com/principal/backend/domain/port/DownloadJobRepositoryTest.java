package com.principal.backend.domain.port;

import com.principal.backend.domain.model.DownloadJob;
import com.principal.backend.domain.model.DownloadStatus;
import com.principal.backend.domain.model.DownloadType;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DownloadJobRepositoryTest {

    @Test
    void shouldFindById() {
        DownloadJobRepository repo = new DownloadJobRepository() {
            @Override
            public Optional<DownloadJob> findById(UUID id) {
                return Optional.empty();
            }

            @Override
            public List<DownloadJob> findByUserId(UUID userId) {
                return List.of();
            }

            @Override
            public void save(DownloadJob job) {}

            @Override
            public void deleteById(UUID id) {}
        };

        Optional<DownloadJob> result = repo.findById(UUID.randomUUID());
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldSupportSaveAndFindById() {
        DownloadJobRepository repo = new DownloadJobRepository() {
            private DownloadJob stored;

            @Override
            public Optional<DownloadJob> findById(UUID id) {
                return stored != null && stored.getId().equals(id)
                        ? Optional.of(stored) : Optional.empty();
            }

            @Override
            public List<DownloadJob> findByUserId(UUID userId) {
                return stored != null && stored.getUserId().equals(userId)
                        ? List.of(stored) : List.of();
            }

            @Override
            public void save(DownloadJob job) {
                this.stored = job;
            }

            @Override
            public void deleteById(UUID id) {
                if (stored != null && stored.getId().equals(id)) {
                    stored = null;
                }
            }
        };

        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Instant now = Instant.now();
        DownloadJob job = new DownloadJob(id, userId, DownloadStatus.PENDING, DownloadType.AUDIO,
                0, "https://youtube.com/watch?v=dQw4w9WgXcQ", null, now, now);

        repo.save(job);
        assertTrue(repo.findById(id).isPresent());
        assertEquals(DownloadStatus.PENDING, repo.findById(id).get().getStatus());
    }

    @Test
    void shouldFindByUserId() {
        DownloadJobRepository repo = new DownloadJobRepository() {
            private DownloadJob stored;

            @Override
            public Optional<DownloadJob> findById(UUID id) {
                return stored != null && stored.getId().equals(id)
                        ? Optional.of(stored) : Optional.empty();
            }

            @Override
            public List<DownloadJob> findByUserId(UUID userId) {
                return stored != null && stored.getUserId().equals(userId)
                        ? List.of(stored) : List.of();
            }

            @Override
            public void save(DownloadJob job) {
                this.stored = job;
            }

            @Override
            public void deleteById(UUID id) {
                if (stored != null && stored.getId().equals(id)) {
                    stored = null;
                }
            }
        };

        UUID userId = UUID.randomUUID();
        DownloadJob job = new DownloadJob(UUID.randomUUID(), userId, DownloadStatus.PENDING, DownloadType.VIDEO,
                0, "https://youtu.be/dQw4w9WgXcQ", null, Instant.now(), Instant.now());
        repo.save(job);

        List<DownloadJob> results = repo.findByUserId(userId);
        assertEquals(1, results.size());
        assertEquals(userId, results.get(0).getUserId());
    }

    @Test
    void shouldDeleteById() {
        DownloadJobRepository repo = new DownloadJobRepository() {
            private DownloadJob stored;

            @Override
            public Optional<DownloadJob> findById(UUID id) {
                return stored != null && stored.getId().equals(id)
                        ? Optional.of(stored) : Optional.empty();
            }

            @Override
            public List<DownloadJob> findByUserId(UUID userId) {
                return stored != null && stored.getUserId().equals(userId)
                        ? List.of(stored) : List.of();
            }

            @Override
            public void save(DownloadJob job) {
                this.stored = job;
            }

            @Override
            public void deleteById(UUID id) {
                if (stored != null && stored.getId().equals(id)) {
                    stored = null;
                }
            }
        };

        UUID id = UUID.randomUUID();
        repo.save(new DownloadJob(id, UUID.randomUUID(), DownloadStatus.PENDING, DownloadType.AUDIO,
                0, "https://youtube.com/watch?v=dQw4w9WgXcQ", null, Instant.now(), Instant.now()));
        repo.deleteById(id);

        assertTrue(repo.findById(id).isEmpty());
    }
}
