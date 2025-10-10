package com.principal.backend.store;

public class StoredToken {
    private String accessToken;
    private String refreshRoken;
    private long expiresAtMillis;
    private String id;

    public StoredToken(){}

    public StoredToken(String id, String accessToken, String refreshRoken, long expiresAtMillis) {
        this.id = id;
        this.accessToken = accessToken;
        this.refreshRoken = refreshRoken;
        this.expiresAtMillis = expiresAtMillis;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshRoken() {
        return refreshRoken;
    }

    public void setRefreshRoken(String refreshRoken) {
        this.refreshRoken = refreshRoken;
    }

    public long getExpiresAtMillis() {
        return expiresAtMillis;
    }

    public void setExpiresAtMillis(long expiresAtMillis) {
        this.expiresAtMillis = expiresAtMillis;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
