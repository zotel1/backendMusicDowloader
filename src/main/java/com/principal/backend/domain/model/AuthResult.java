package com.principal.backend.domain.model;

public class AuthResult {

    private final String email;
    private final String userId;
    private final boolean success;

    public AuthResult(String email, String userId, boolean success) {
        this.email = email;
        this.userId = userId;
        this.success = success;
    }

    public String getEmail() {
        return email;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isSuccess() {
        return success;
    }
}
