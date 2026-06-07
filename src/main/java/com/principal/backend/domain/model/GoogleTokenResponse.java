package com.principal.backend.domain.model;

import java.time.Instant;

public class GoogleTokenResponse {

    private final String googleId;
    private final String email;
    private final String accessToken;
    private final String refreshToken;
    private final Instant expiresAt;

    public GoogleTokenResponse(String googleId, String email, String accessToken,
                               String refreshToken, Instant expiresAt) {
        this.googleId = googleId;
        this.email = email;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
    }

    public String getGoogleId() { return googleId; }
    public String getEmail() { return email; }
    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public Instant getExpiresAt() { return expiresAt; }
}
