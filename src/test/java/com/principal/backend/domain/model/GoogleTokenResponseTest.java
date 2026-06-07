package com.principal.backend.domain.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class GoogleTokenResponseTest {

    @Test
    void shouldCreateGoogleTokenResponseWithAllFields() {
        String googleId = "google-user-456";
        String email = "test@gmail.com";
        String accessToken = "ya29.a0AfH6SMB...";
        String refreshToken = "1//0gX4W7...";
        Instant expiresAt = Instant.now().plusSeconds(3600);

        GoogleTokenResponse response = new GoogleTokenResponse(googleId, email, accessToken, refreshToken, expiresAt);

        assertEquals(googleId, response.getGoogleId());
        assertEquals(email, response.getEmail());
        assertEquals(accessToken, response.getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());
        assertEquals(expiresAt, response.getExpiresAt());
    }

    @Test
    void shouldAllowNullRefreshToken() {
        GoogleTokenResponse response = new GoogleTokenResponse("gid", "e@e.com", "at", null, Instant.now());

        assertNull(response.getRefreshToken());
    }
}
