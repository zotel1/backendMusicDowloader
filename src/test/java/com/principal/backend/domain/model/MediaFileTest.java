package com.principal.backend.domain.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MediaFileTest {

    @Test
    void shouldCreateMediaFileWithAllFields() {
        UUID id = UUID.randomUUID();
        UUID jobId = UUID.randomUUID();

        MediaFile file = new MediaFile(
                id, jobId, "Never Gonna Give You Up", "Rick Astley",
                "Whenever You Need Somebody", "3:32",
                "https://i.ytimg.com/vi/dQw4w9WgXcQ/default.jpg",
                "Pop", "RickAstleyVEVO", "1987-07-28", "drive-file-123"
        );

        assertEquals(id, file.getId());
        assertEquals(jobId, file.getJobId());
        assertEquals("Never Gonna Give You Up", file.getTitle());
        assertEquals("Rick Astley", file.getArtist());
        assertEquals("Whenever You Need Somebody", file.getAlbum());
        assertEquals("3:32", file.getDuration());
        assertEquals("https://i.ytimg.com/vi/dQw4w9WgXcQ/default.jpg", file.getThumbnailUrl());
        assertEquals("Pop", file.getGenre());
        assertEquals("RickAstleyVEVO", file.getChannel());
        assertEquals("1987-07-28", file.getUploadDate());
        assertEquals("drive-file-123", file.getGoogleDriveFileId());
    }

    @Test
    void shouldAllowNullOptionalFields() {
        UUID id = UUID.randomUUID();
        UUID jobId = UUID.randomUUID();

        MediaFile file = new MediaFile(
                id, jobId, "Title", null, null, "3:00",
                null, null, null, null, null
        );

        assertEquals("Title", file.getTitle());
        assertNull(file.getArtist());
        assertNull(file.getAlbum());
        assertNull(file.getThumbnailUrl());
        assertNull(file.getGenre());
        assertNull(file.getChannel());
        assertNull(file.getUploadDate());
        assertNull(file.getGoogleDriveFileId());
    }
}
