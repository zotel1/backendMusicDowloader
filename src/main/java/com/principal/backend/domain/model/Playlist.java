package com.principal.backend.domain.model;

import java.util.UUID;

public class Playlist {

    private final UUID id;
    private final UUID ownerId;
    private final String title;
    private final String url;

    public Playlist(UUID id, UUID ownerId, String title, String url) {
        this.id = id;
        this.ownerId = ownerId;
        this.title = title;
        this.url = url;
    }

    public UUID getId() { return id; }
    public UUID getOwnerId() { return ownerId; }
    public String getTitle() { return title; }
    public String getUrl() { return url; }
}
