package com.principal.backend.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class DownloadTypeTest {

    @Test
    void shouldHaveExpectedValues() {
        DownloadType[] values = DownloadType.values();
        assertEquals(3, values.length);
        assertEquals(DownloadType.AUDIO, DownloadType.valueOf("AUDIO"));
        assertEquals(DownloadType.VIDEO, DownloadType.valueOf("VIDEO"));
        assertEquals(DownloadType.PLAYLIST, DownloadType.valueOf("PLAYLIST"));
    }

    @ParameterizedTest
    @CsvSource({
        "youtube.com/watch?v=dQw4w9WgXcQ, VIDEO",
        "www.youtube.com/watch?v=dQw4w9WgXcQ, VIDEO",
        "https://www.youtube.com/watch?v=dQw4w9WgXcQ, VIDEO",
        "youtu.be/dQw4w9WgXcQ, VIDEO",
        "www.youtu.be/dQw4w9WgXcQ, VIDEO",
        "https://youtu.be/dQw4w9WgXcQ, VIDEO"
    })
    void shouldDetectVideoFromWatchUrl(String url, DownloadType expected) {
        assertEquals(expected, DownloadType.fromUrl(url));
    }

    @ParameterizedTest
    @CsvSource({
        "youtube.com/playlist?list=PLrAXtmErZgOeiKm4sgNOknGvNjby9efdf, PLAYLIST",
        "www.youtube.com/playlist?list=PLrAXtmErZgOeiKm4sgNOknGvNjby9efdf, PLAYLIST",
        "https://www.youtube.com/playlist?list=PLrAXtmErZgOeiKm4sgNOknGvNjby9efdf, PLAYLIST"
    })
    void shouldDetectPlaylistFromPlaylistUrl(String url, DownloadType expected) {
        assertEquals(expected, DownloadType.fromUrl(url));
    }

    @ParameterizedTest
    @CsvSource({
        "https://vimeo.com/123456",
        "https://www.dailymotion.com/video/abc123",
        "invalid-url",
        "https://youtube.com/unknown"
    })
    void shouldThrowOnUnrecognizedUrl(String url) {
        assertThrows(IllegalArgumentException.class, () -> DownloadType.fromUrl(url));
    }

    @Test
    void shouldThrowOnBlankUrl() {
        assertThrows(IllegalArgumentException.class, () -> DownloadType.fromUrl(""));
        assertThrows(IllegalArgumentException.class, () -> DownloadType.fromUrl(null));
    }
}
