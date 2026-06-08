package com.principal.backend.application.strategy;

import com.principal.backend.domain.model.DownloadType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DownloadStrategyFactoryTest {

    @Test
    void getStrategy_ReturnsAudioStrategy_ForAUDIO() {
        AudioDownloadStrategy audio = new AudioDownloadStrategy(null, null);
        VideoDownloadStrategy video = new VideoDownloadStrategy(null, null);
        PlaylistDownloadStrategy playlist = new PlaylistDownloadStrategy(null, null);
        DownloadStrategyFactory factory = new DownloadStrategyFactory(List.of(audio, video, playlist));

        DownloadStrategy result = factory.getStrategy(DownloadType.AUDIO);

        assertInstanceOf(AudioDownloadStrategy.class, result);
    }

    @Test
    void getStrategy_ReturnsVideoStrategy_ForVIDEO() {
        AudioDownloadStrategy audio = new AudioDownloadStrategy(null, null);
        VideoDownloadStrategy video = new VideoDownloadStrategy(null, null);
        PlaylistDownloadStrategy playlist = new PlaylistDownloadStrategy(null, null);
        DownloadStrategyFactory factory = new DownloadStrategyFactory(List.of(audio, video, playlist));

        DownloadStrategy result = factory.getStrategy(DownloadType.VIDEO);

        assertInstanceOf(VideoDownloadStrategy.class, result);
    }

    @Test
    void getStrategy_ReturnsPlaylistStrategy_ForPLAYLIST() {
        AudioDownloadStrategy audio = new AudioDownloadStrategy(null, null);
        VideoDownloadStrategy video = new VideoDownloadStrategy(null, null);
        PlaylistDownloadStrategy playlist = new PlaylistDownloadStrategy(null, null);
        DownloadStrategyFactory factory = new DownloadStrategyFactory(List.of(audio, video, playlist));

        DownloadStrategy result = factory.getStrategy(DownloadType.PLAYLIST);

        assertInstanceOf(PlaylistDownloadStrategy.class, result);
    }

    @Test
    void getStrategy_WhenNoMatch_Throws() {
        AudioDownloadStrategy audio = new AudioDownloadStrategy(null, null);
        PlaylistDownloadStrategy playlist = new PlaylistDownloadStrategy(null, null);
        DownloadStrategyFactory factory = new DownloadStrategyFactory(List.of(audio, playlist));

        assertThrows(IllegalArgumentException.class, () -> factory.getStrategy(DownloadType.VIDEO));
    }
}
