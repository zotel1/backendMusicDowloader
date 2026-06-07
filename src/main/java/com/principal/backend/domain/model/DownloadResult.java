package com.principal.backend.domain.model;

public class DownloadResult {

    private final String status;
    private final String message;
    private final String filePath;

    public DownloadResult(String status, String message, String filePath) {
        this.status = status;
        this.message = message;
        this.filePath = filePath;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        if (filePath == null || filePath.isEmpty()) {
            return null;
        }
        int lastBackslash = filePath.lastIndexOf('\\');
        int lastSlash = filePath.lastIndexOf('/');
        int lastSeparator = Math.max(lastBackslash, lastSlash);
        if (lastSeparator == -1) {
            return filePath;
        }
        return filePath.substring(lastSeparator + 1);
    }
}
