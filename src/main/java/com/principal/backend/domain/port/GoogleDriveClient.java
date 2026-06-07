package com.principal.backend.domain.port;

import java.io.File;

public interface GoogleDriveClient {

    void uploadFile(String accessToken, File file, String mimeType);
}
