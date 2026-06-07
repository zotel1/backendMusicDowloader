package com.principal.backend.infrastructure.adapter.google;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoogleDriveApiAdapterTest {

    @Mock
    private Drive drive;

    @Mock
    private Drive.Files files;

    @Mock
    private Drive.Files.List listRequest;

    @Mock
    private Drive.Files.Create createRequest;

    private GoogleDriveApiAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new GoogleDriveApiAdapter(drive);
    }

    @Test
    void createFolders_CreatesFromScratch() throws IOException {
        when(drive.files()).thenReturn(files);
        when(files.list()).thenReturn(listRequest);
        when(listRequest.setQ(anyString())).thenReturn(listRequest);
        when(listRequest.setSpaces(anyString())).thenReturn(listRequest);
        when(listRequest.setFields(anyString())).thenReturn(listRequest);

        FileList emptyFileList = new FileList();
        emptyFileList.setFiles(Collections.emptyList());
        when(listRequest.execute()).thenReturn(emptyFileList);

        when(files.create(any(File.class))).thenReturn(createRequest);
        when(createRequest.setFields(anyString())).thenReturn(createRequest);

        File musicDownloaderFolder = new File();
        musicDownloaderFolder.setId("root-folder-id");
        musicDownloaderFolder.setName("Music Downloader");
        when(createRequest.execute()).thenReturn(musicDownloaderFolder);

        FileList emptySubList = new FileList();
        emptySubList.setFiles(Collections.emptyList());
        when(listRequest.execute())
                .thenReturn(emptyFileList)
                .thenReturn(emptySubList)
                .thenReturn(emptySubList);

        adapter.createFolders("test-access-token");

        verify(files, times(3)).create(any(File.class));
        verify(createRequest, times(3)).execute();
    }

    @Test
    void createFolders_SkipsExistingFolder() throws IOException {
        when(drive.files()).thenReturn(files);
        when(files.list()).thenReturn(listRequest);
        when(listRequest.setQ(anyString())).thenReturn(listRequest);
        when(listRequest.setSpaces(anyString())).thenReturn(listRequest);
        when(listRequest.setFields(anyString())).thenReturn(listRequest);

        File existingFolder = new File();
        existingFolder.setId("existing-root-id");
        existingFolder.setName("Music Downloader");
        FileList existingFileList = new FileList();
        existingFileList.setFiles(List.of(existingFolder));
        when(listRequest.execute()).thenReturn(existingFileList);

        FileList emptySubList = new FileList();
        emptySubList.setFiles(Collections.emptyList());
        when(listRequest.execute())
                .thenReturn(existingFileList)
                .thenReturn(emptySubList)
                .thenReturn(emptySubList);

        when(files.create(any(File.class))).thenReturn(createRequest);
        when(createRequest.setFields(anyString())).thenReturn(createRequest);

        File musicFolder = new File();
        musicFolder.setId("music-id");
        musicFolder.setName("Music");
        when(createRequest.execute()).thenReturn(musicFolder);

        adapter.createFolders("test-access-token");

        verify(files, times(2)).create(any(File.class));
    }

    @Test
    void createFolders_SkipsAllExistingFolders() throws IOException {
        adapter = new GoogleDriveApiAdapter(drive);
        when(drive.files()).thenReturn(files);
        when(files.list()).thenReturn(listRequest);
        when(listRequest.setQ(anyString())).thenReturn(listRequest);
        when(listRequest.setSpaces(anyString())).thenReturn(listRequest);
        when(listRequest.setFields(anyString())).thenReturn(listRequest);

        File rootFolder = new File();
        rootFolder.setId("root-id");
        rootFolder.setName("Music Downloader");
        FileList rootList = new FileList();
        rootList.setFiles(List.of(rootFolder));

        File musicFolder = new File();
        musicFolder.setId("music-id");
        musicFolder.setName("Music");
        FileList musicList = new FileList();
        musicList.setFiles(List.of(musicFolder));

        File videosFolder = new File();
        videosFolder.setId("videos-id");
        videosFolder.setName("Videos");
        FileList videosList = new FileList();
        videosList.setFiles(List.of(videosFolder));

        when(listRequest.execute())
                .thenReturn(rootList)
                .thenReturn(musicList)
                .thenReturn(videosList);

        adapter.createFolders("test-access-token");

        verify(files, never()).create(any(File.class));
    }

    @Test
    void createFolders_LogsWarningOnError() throws IOException {
        when(drive.files()).thenReturn(files);
        when(files.list()).thenReturn(listRequest);
        when(listRequest.setQ(anyString())).thenReturn(listRequest);
        when(listRequest.setSpaces(anyString())).thenReturn(listRequest);
        when(listRequest.setFields(anyString())).thenReturn(listRequest);

        when(listRequest.execute()).thenThrow(new RuntimeException("Drive unavailable"));

        assertDoesNotThrow(() -> adapter.createFolders("test-access-token"));
    }
}
