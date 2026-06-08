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
class AudioDownloadStrategyTest {

    @Mock
    private PythonDownloadClient pythonClient;

    @Mock
    private GoogleDriveClient driveClient;

    @InjectMocks
    private AudioDownloadStrategy strategy;

    @Test
    void getType_ReturnsAUDIO() {
        assertEquals(DownloadType.AUDIO, strategy.getType());
    }

    @Test
    void execute_CallsPythonAndDrive_ReturnsSuccess() throws IOException {
        Path tempFile = Files.createTempFile("test-audio", ".mp3");
        tempFile.toFile().deleteOnExit();
        DownloadResult pythonResult = new DownloadResult("success", "ok", tempFile.toString());
        when(pythonClient.download("https://youtube.com/watch?v=test")).thenReturn(pythonResult);

        DownloadResult result = strategy.execute("https://youtube.com/watch?v=test", "token123");

        assertEquals("success", result.getStatus());
        assertTrue(result.getMessage().contains("Audio"));
        verify(pythonClient).download("https://youtube.com/watch?v=test");
        verify(driveClient).uploadFile(eq("token123"), any(java.io.File.class), anyString());
    }

    @Test
    void execute_WhenPythonReturnsNull_ReturnsError() {
        when(pythonClient.download("bad-url")).thenReturn(null);

        DownloadResult result = strategy.execute("bad-url", "token");

        assertEquals("error", result.getStatus());
        verifyNoInteractions(driveClient);
    }
}
