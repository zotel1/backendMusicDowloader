package com.principal.backend.domain.port;

import com.principal.backend.domain.model.MediaFile;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MediaFileRepositoryTest {

    @Test
    void shouldFindByDownloadJobId() {
        MediaFileRepository repo = new MediaFileRepository() {
            @Override
            public Optional<MediaFile> findByDownloadJobId(UUID jobId) {
                return Optional.empty();
            }

            @Override
            public void save(MediaFile mediaFile) {}
        };

        Optional<MediaFile> result = repo.findByDownloadJobId(UUID.randomUUID());
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldSupportSaveAndFind() {
        MediaFileRepository repo = new MediaFileRepository() {
            private MediaFile stored;

            @Override
            public Optional<MediaFile> findByDownloadJobId(UUID jobId) {
                return stored != null && stored.getJobId().equals(jobId)
                        ? Optional.of(stored) : Optional.empty();
            }

            @Override
            public void save(MediaFile mediaFile) {
                this.stored = mediaFile;
            }
        };

        UUID jobId = UUID.randomUUID();
        MediaFile file = new MediaFile(UUID.randomUUID(), jobId, "Title", "Artist", null,
                "3:30", null, null, null, null, null);

        repo.save(file);
        assertTrue(repo.findByDownloadJobId(jobId).isPresent());
        assertEquals("Title", repo.findByDownloadJobId(jobId).get().getTitle());
    }
}
