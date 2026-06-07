package com.principal.backend.domain.model;

import java.time.Instant;
import java.util.UUID;

public class User {

    private final UUID id;
    private final String email;
    private final String password;
    private final Role role;
    private final UserStatus status;
    private final Instant createdAt;

    public User(UUID id, String email, String password, Role role, UserStatus status, Instant createdAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static User create(String email, String password) {
        return new User(
                UUID.randomUUID(),
                email,
                password,
                Role.ROLE_USER,
                UserStatus.ACTIVE,
                Instant.now()
        );
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public UserStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
