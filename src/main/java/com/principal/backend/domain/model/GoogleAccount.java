package com.principal.backend.domain.model;

import java.time.Instant;
import java.util.UUID;

public class GoogleAccount {

    private final UUID id;
    private final UUID userId;
    private final String googleId;
    private final String email;
    private String accessToken;
    private String refreshToken;
    private Instant expiresAt;

    public GoogleAccount(UUID id, UUID userId, String googleId, String email,
                         String accessToken, String refreshToken, Instant expiresAt) {
        this.id = id;
        this.userId = userId;
        this.googleId = googleId;
        this.email = email;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
    }

    public UUID getId() { return id; }
    public UUID getUserId() { return userId; }
    public String getGoogleId() { return googleId; }
    public String getEmail() { return email; }
    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public Instant getExpiresAt() { return expiresAt; }

    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }
}
