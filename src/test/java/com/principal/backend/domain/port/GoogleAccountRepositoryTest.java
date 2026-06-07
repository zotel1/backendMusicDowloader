package com.principal.backend.domain.port;

import com.principal.backend.domain.model.GoogleAccount;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class GoogleAccountRepositoryTest {

    @Test
    void shouldFindByUserId() {
        GoogleAccountRepository repo = new GoogleAccountRepository() {
            @Override
            public Optional<GoogleAccount> findByUserId(UUID userId) {
                return Optional.empty();
            }

            @Override
            public void save(GoogleAccount account) {}

            @Override
            public void deleteByUserId(UUID userId) {}
        };

        Optional<GoogleAccount> result = repo.findByUserId(UUID.randomUUID());
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldSupportSaveOperation() {
        GoogleAccountRepository repo = new GoogleAccountRepository() {
            private GoogleAccount stored;

            @Override
            public Optional<GoogleAccount> findByUserId(UUID userId) {
                return Optional.ofNullable(stored);
            }

            @Override
            public void save(GoogleAccount account) {
                this.stored = account;
            }

            @Override
            public void deleteByUserId(UUID userId) {
                stored = null;
            }
        };

        UUID userId = UUID.randomUUID();
        GoogleAccount account = new GoogleAccount(UUID.randomUUID(), userId, "gid", "e@e.com",
                "at", "rt", Instant.now().plusSeconds(3600));

        repo.save(account);
        assertTrue(repo.findByUserId(userId).isPresent());
        assertEquals("gid", repo.findByUserId(userId).get().getGoogleId());
    }

    @Test
    void shouldDeleteByUserId() {
        GoogleAccountRepository repo = new GoogleAccountRepository() {
            private GoogleAccount stored;

            @Override
            public Optional<GoogleAccount> findByUserId(UUID userId) {
                return Optional.ofNullable(stored);
            }

            @Override
            public void save(GoogleAccount account) {
                this.stored = account;
            }

            @Override
            public void deleteByUserId(UUID userId) {
                stored = null;
            }
        };

        UUID userId = UUID.randomUUID();
        repo.save(new GoogleAccount(UUID.randomUUID(), userId, "gid", "e@e.com",
                "at", "rt", Instant.now().plusSeconds(3600)));
        repo.deleteByUserId(userId);

        assertTrue(repo.findByUserId(userId).isEmpty());
    }
}
