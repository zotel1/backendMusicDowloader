package com.principal.backend.infrastructure.adapter.jpa.entity;

import com.principal.backend.domain.model.Playlist;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "playlists")
public class PlaylistEntity {
    @Id private UUID id;
    @Column(name = "owner_id", nullable = false) private UUID ownerId;
    @Column(nullable = false) private String title;
    @Column(nullable = false, columnDefinition = "TEXT") private String url;
    @Column(name = "created_at", nullable = false) private Instant createdAt;

    public PlaylistEntity() {}

    public static PlaylistEntity fromDomain(Playlist pl) {
        PlaylistEntity e = new PlaylistEntity();
        e.id = pl.getId(); e.ownerId = pl.getOwnerId(); e.title = pl.getTitle(); e.url = pl.getUrl();
        e.createdAt = Instant.now();
        return e;
    }
    public Playlist toDomain() { return new Playlist(id, ownerId, title, url); }

    public UUID getId() { return id; } public void setId(UUID id) { this.id = id; }
    public UUID getOwnerId() { return ownerId; } public void setOwnerId(UUID ownerId) { this.ownerId = ownerId; }
    public String getTitle() { return title; } public void setTitle(String title) { this.title = title; }
    public String getUrl() { return url; } public void setUrl(String url) { this.url = url; }
    public Instant getCreatedAt() { return createdAt; } public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
