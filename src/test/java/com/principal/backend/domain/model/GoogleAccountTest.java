package com.principal.backend.domain.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class GoogleAccountTest {

    @Test
    void shouldCreateGoogleAccountWithAllFields() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String googleId = "google-user-123";
        String email = "user@gmail.com";
        String accessToken = "ya29.a0AfH6SMB...";
        String refreshToken = "1//0gX4W7...";
        Instant expiresAt = Instant.now().plusSeconds(3600);

        GoogleAccount account = new GoogleAccount(id, userId, googleId, email, accessToken, refreshToken, expiresAt);

        assertEquals(id, account.getId());
        assertEquals(userId, account.getUserId());
        assertEquals(googleId, account.getGoogleId());
        assertEquals(email, account.getEmail());
        assertEquals(accessToken, account.getAccessToken());
        assertEquals(refreshToken, account.getRefreshToken());
        assertEquals(expiresAt, account.getExpiresAt());
    }

    @Test
    void shouldAllowNullRefreshToken() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Instant expiresAt = Instant.now().plusSeconds(3600);

        GoogleAccount account = new GoogleAccount(id, userId, "gid", "e@e.com", "at", null, expiresAt);

        assertNull(account.getRefreshToken());
    }

    @Test
    void shouldSetUpdatedAccessTokenAndExpiry() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Instant expiresAt = Instant.now().plusSeconds(3600);

        GoogleAccount account = new GoogleAccount(id, userId, "gid", "e@e.com", "old-token", "rt", expiresAt);

        Instant newExpiry = Instant.now().plusSeconds(7200);
        account.setAccessToken("new-token");
        account.setExpiresAt(newExpiry);

        assertEquals("new-token", account.getAccessToken());
        assertEquals(newExpiry, account.getExpiresAt());
    }
}
