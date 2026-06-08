package com.principal.backend.domain.model;

public enum DownloadType {
    AUDIO,
    VIDEO,
    PLAYLIST;

    private static final String YOUTUBE_WATCH = "/watch";
    private static final String YOUTUBE_PLAYLIST = "/playlist";
    private static final String YOUTU_BE = "youtu.be";

    public static DownloadType fromUrl(String url) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("URL must not be null or blank");
        }

        String normalized = url.toLowerCase();

        if (normalized.contains(YOUTU_BE)) {
            return VIDEO;
        }

        if (!normalized.contains("youtube.com")) {
            throw new IllegalArgumentException("Unsupported URL: " + url);
        }

        String path = extractPath(normalized);

        if (path.startsWith(YOUTUBE_PLAYLIST)) {
            return PLAYLIST;
        }

        if (path.startsWith(YOUTUBE_WATCH)) {
            return VIDEO;
        }

        throw new IllegalArgumentException("Unsupported YouTube URL: " + url);
    }

    private static String extractPath(String url) {
        int protocolEnd = url.indexOf("://");
        int start = (protocolEnd != -1) ? protocolEnd + 3 : 0;

        int pathStart = url.indexOf("/", start);
        if (pathStart == -1) return "";

        int queryStart = url.indexOf("?", pathStart);
        return (queryStart != -1)
                ? url.substring(pathStart, queryStart)
                : url.substring(pathStart);
    }
}
