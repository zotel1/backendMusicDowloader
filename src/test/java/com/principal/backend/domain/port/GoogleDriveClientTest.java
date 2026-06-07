package com.principal.backend.domain.port;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class GoogleDriveClientTest {

    @Test
    void shouldSupportCreateFolders() {
        GoogleDriveClient client = new GoogleDriveClient() {
            private boolean foldersCreated = false;

            @Override
            public void uploadFile(String accessToken, File file, String mimeType) {}

            @Override
            public void createFolders(String accessToken) {
                this.foldersCreated = true;
            }

            public boolean isFoldersCreated() { return foldersCreated; }
        };

        client.createFolders("some-access-token");

        // Verify the method was called without exception
        assertDoesNotThrow(() -> client.createFolders("access-token"));
    }
}
