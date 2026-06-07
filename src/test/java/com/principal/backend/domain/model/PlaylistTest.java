package com.principal.backend.domain.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PlaylistTest {

    @Test
    void shouldCreatePlaylistWithAllFields() {
        UUID id = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        Playlist playlist = new Playlist(id, ownerId, "My Favorites",
                "https://youtube.com/playlist?list=PLrAXtmErZgOeiKm4sgNOknGvNjby9efdf");

        assertEquals(id, playlist.getId());
        assertEquals(ownerId, playlist.getOwnerId());
        assertEquals("My Favorites", playlist.getTitle());
        assertEquals("https://youtube.com/playlist?list=PLrAXtmErZgOeiKm4sgNOknGvNjby9efdf", playlist.getUrl());
    }
}
