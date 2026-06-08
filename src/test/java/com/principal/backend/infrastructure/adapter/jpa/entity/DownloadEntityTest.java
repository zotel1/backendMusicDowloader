package com.principal.backend.infrastructure.adapter.jpa.entity;

import com.principal.backend.domain.model.*;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class DownloadEntityTest {
    @Test void downloadJobRoundtrip() {
        UUID id = UUID.randomUUID(), userId = UUID.randomUUID();
        Instant now = Instant.now();
        DownloadJob domain = new DownloadJob(id, userId, DownloadStatus.PENDING, DownloadType.AUDIO,
                0, "https://youtube.com/watch?v=abc", null, now, now);
        UserEntity ue = new UserEntity(); ue.setId(userId);
        DownloadJobEntity e = DownloadJobEntity.fromDomain(domain, ue);
        assertEquals(id, e.getId()); assertEquals(userId, e.getUser().getId());
        assertEquals(DownloadStatus.PENDING, e.getStatus()); assertEquals(DownloadType.AUDIO, e.getType());
        assertEquals(0, e.getProgress()); assertEquals("https://youtube.com/watch?v=abc", e.getSourceUrl());
        assertNotNull(e.getCreatedAt()); assertNotNull(e.getUpdatedAt());
        DownloadJob rt = e.toDomain();
        assertEquals(id, rt.getId()); assertEquals(userId, rt.getUserId()); assertEquals(DownloadStatus.PENDING, rt.getStatus());
    }
    @Test void mediaFileRoundtrip() {
        UUID id = UUID.randomUUID(), jobId = UUID.randomUUID();
        MediaFile domain = new MediaFile(id, jobId, "Title", "Artist", "Album", "3:30",
                "https://thumb.url", "Pop", "Channel", "2024-01-01", "drive123");
        MediaFileEntity e = MediaFileEntity.fromDomain(domain);
        assertEquals(id, e.getId()); assertEquals(jobId, e.getJobId()); assertEquals("Title", e.getTitle());
        assertEquals("Artist", e.getArtist()); assertEquals("Album", e.getAlbum()); assertEquals("3:30", e.getDuration());
        assertEquals("drive123", e.getGoogleDriveFileId());
        MediaFile rt = e.toDomain();
        assertEquals(id, rt.getId()); assertEquals(jobId, rt.getJobId()); assertEquals("Title", rt.getTitle());
    }
    @Test void playlistRoundtrip() {
        UUID id = UUID.randomUUID(), ownerId = UUID.randomUUID();
        Playlist domain = new Playlist(id, ownerId, "My Playlist", "https://youtube.com/playlist?list=abc");
        PlaylistEntity e = PlaylistEntity.fromDomain(domain);
        assertEquals(id, e.getId()); assertEquals(ownerId, e.getOwnerId()); assertEquals("My Playlist", e.getTitle());
        assertEquals("https://youtube.com/playlist?list=abc", e.getUrl());
        Playlist rt = e.toDomain();
        assertEquals(id, rt.getId()); assertEquals(ownerId, rt.getOwnerId());
    }
}
