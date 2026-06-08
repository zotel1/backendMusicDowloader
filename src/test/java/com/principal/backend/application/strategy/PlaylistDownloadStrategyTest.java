package com.principal.backend.application.strategy;

import com.principal.backend.domain.model.DownloadResult;
import com.principal.backend.domain.model.DownloadType;
import com.principal.backend.domain.port.GoogleDriveClient;
import com.principal.backend.domain.port.PythonDownloadClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlaylistDownloadStrategyTest {

    @Mock
    private PythonDownloadClient pythonClient;

    @Mock
    private GoogleDriveClient driveClient;

    @InjectMocks
    private PlaylistDownloadStrategy strategy;

    @Test
    void getType_ReturnsPLAYLIST() {
        assertEquals(DownloadType.PLAYLIST, strategy.getType());
    }

    @Test
    void execute_CallsPythonAndDrive_ReturnsSuccess() throws IOException {
        Path tempFile = Files.createTempFile("test-playlist", ".mp3");
        tempFile.toFile().deleteOnExit();
        DownloadResult pythonResult = new DownloadResult("success", "ok", tempFile.toString());
        when(pythonClient.download("https://youtube.com/playlist?list=abc")).thenReturn(pythonResult);

        DownloadResult result = strategy.execute("https://youtube.com/playlist?list=abc", "token123");

        assertEquals("success", result.getStatus());
        assertTrue(result.getMessage().contains("Playlist"));
        verify(pythonClient).download("https://youtube.com/playlist?list=abc");
        verify(driveClient).uploadFile(eq("token123"), any(java.io.File.class), anyString());
    }

    @Test
    void execute_WhenPythonThrows_ReturnsError() {
        when(pythonClient.download("bad-url")).thenThrow(new RuntimeException("API down"));

        DownloadResult result = strategy.execute("bad-url", "token");

        assertEquals("error", result.getStatus());
        verifyNoInteractions(driveClient);
    }
}
