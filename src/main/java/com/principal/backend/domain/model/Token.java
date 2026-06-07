package com.principal.backend.domain.model;

public class Token {

    private final String userId;
    private final String accessToken;
    private final String refreshToken;
    private final long expiresAtMillis;

    public Token(String userId, String accessToken, String refreshToken, long expiresAtMillis) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresAtMillis = expiresAtMillis;
    }

    public String getUserId() {
        return userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public long getExpiresAtMillis() {
        return expiresAtMillis;
    }
}
