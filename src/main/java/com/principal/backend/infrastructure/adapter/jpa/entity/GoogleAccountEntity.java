package com.principal.backend.infrastructure.adapter.jpa.entity;

import com.principal.backend.domain.model.GoogleAccount;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "google_accounts")
public class GoogleAccountEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    @Column(nullable = false, unique = true)
    private String googleId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String accessToken;

    @Column(columnDefinition = "TEXT")
    private String refreshToken;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public GoogleAccountEntity() {}

    public static GoogleAccountEntity fromDomain(GoogleAccount account, UserEntity userEntity) {
        GoogleAccountEntity e = new GoogleAccountEntity();
        e.id = account.getId();
        e.user = userEntity;
        e.googleId = account.getGoogleId();
        e.email = account.getEmail();
        e.accessToken = account.getAccessToken();
        e.refreshToken = account.getRefreshToken();
        e.expiresAt = account.getExpiresAt();
        e.createdAt = Instant.now();
        return e;
    }

    public GoogleAccount toDomain() {
        return new GoogleAccount(id, user.getId(), googleId, email, accessToken, refreshToken, expiresAt);
    }

    // Getters and setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UserEntity getUser() { return user; }
    public void setUser(UserEntity user) { this.user = user; }
    public String getGoogleId() { return googleId; }
    public void setGoogleId(String googleId) { this.googleId = googleId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    public Instant getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
