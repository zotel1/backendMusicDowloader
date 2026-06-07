package com.principal.backend.infrastructure.adapter.jpa.entity;

import com.principal.backend.domain.model.Role;
import com.principal.backend.domain.model.User;
import com.principal.backend.domain.model.UserStatus;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    // Default constructor for JPA
    public UserEntity() {}

    // Constructor from domain
    public static UserEntity fromDomain(User user) {
        UserEntity e = new UserEntity();
        e.id = user.getId();
        e.email = user.getEmail();
        e.password = user.getPassword();
        e.role = user.getRole();
        e.status = user.getStatus();
        e.createdAt = user.getCreatedAt();
        return e;
    }

    // To domain
    public User toDomain() {
        return new User(id, email, password, role, status, createdAt);
    }

    // Getters and setters (JPA needs setters)
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
