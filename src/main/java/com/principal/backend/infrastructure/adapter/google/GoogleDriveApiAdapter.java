package com.principal.backend.infrastructure.adapter.google;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.principal.backend.domain.port.GoogleDriveClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Component
public class GoogleDriveApiAdapter implements GoogleDriveClient {

    private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final Logger log = LoggerFactory.getLogger(GoogleDriveApiAdapter.class);
    private static final String FOLDER_MIME_TYPE = "application/vnd.google-apps.folder";
    private static final String ROOT_FOLDER_NAME = "Music Downloader";
    private static final String MUSIC_FOLDER_NAME = "Music";
    private static final String VIDEOS_FOLDER_NAME = "Videos";

    private final Drive driveInstance;

    public GoogleDriveApiAdapter() {
        this.driveInstance = null;
    }

    // Package-private for testing
    GoogleDriveApiAdapter(Drive drive) {
        this.driveInstance = drive;
    }

    @Override
    public void createFolders(String accessToken) {
        try {
            Drive drive = (driveInstance != null) ? driveInstance : buildDriveWithAccessToken(accessToken);
            createFoldersInternal(drive);
        } catch (Exception e) {
            log.warn("Failed to create Google Drive folders: {}", e.getMessage());
        }
    }

    private void createFoldersInternal(Drive drive) throws IOException {
        // 1. Find or create "Music Downloader" root folder
        String rootFolderId = findOrCreateFolder(drive, ROOT_FOLDER_NAME, null);

        // 2. Find or create "Music" subfolder
        findOrCreateFolder(drive, MUSIC_FOLDER_NAME, rootFolderId);

        // 3. Find or create "Videos" subfolder
        findOrCreateFolder(drive, VIDEOS_FOLDER_NAME, rootFolderId);
    }

    private String findOrCreateFolder(Drive drive, String folderName, String parentId) throws IOException {
        String query = "name = '" + folderName + "'"
                + " and mimeType = '" + FOLDER_MIME_TYPE + "'"
                + " and trashed = false";
        if (parentId != null) {
            query = "'" + parentId + "' in parents and " + query;
        }

        FileList result = drive.files().list()
                .setQ(query)
                .setSpaces("drive")
                .setFields("files(id, name)")
                .execute();

        List<File> files = result.getFiles();
        if (files != null && !files.isEmpty()) {
            log.debug("Folder '{}' already exists with id {}", folderName, files.get(0).getId());
            return files.get(0).getId();
        }

        // Create folder
        File folderMetadata = new File();
        folderMetadata.setName(folderName);
        folderMetadata.setMimeType(FOLDER_MIME_TYPE);
        if (parentId != null) {
            folderMetadata.setParents(Collections.singletonList(parentId));
        }

        File createdFolder = drive.files().create(folderMetadata)
                .setFields("id, name")
                .execute();

        log.info("Created folder '{}' with id {}", folderName, createdFolder.getId());
        return createdFolder.getId();
    }

    @Override
    public void uploadFile(String accessToken, java.io.File localFile, String mimeType) {
        try {
            Drive drive = buildDriveWithAccessToken(accessToken);

            File fileMetadata = new File();
            fileMetadata.setName(localFile.getName());

            FileContent mediaContent = new FileContent(mimeType, localFile);
            drive.files().create(fileMetadata, mediaContent)
                    .setFields("id, name")
                    .execute();
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to Google Drive: " + e.getMessage(), e);
        }
    }

    private Drive buildDriveWithAccessToken(String accessToken) throws GeneralSecurityException, IOException {
        var transport = GoogleNetHttpTransport.newTrustedTransport();
        var initializer = (com.google.api.client.http.HttpRequestInitializer) request ->
                request.getHeaders().setAuthorization("Bearer " + accessToken);

        return new Drive.Builder(transport, JSON_FACTORY, initializer)
                .setApplicationName("Downloader App")
                .build();
    }
}
