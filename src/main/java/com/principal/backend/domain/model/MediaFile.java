package com.principal.backend.domain.model;

import java.util.UUID;

public class MediaFile {

    private final UUID id;
    private final UUID jobId;
    private final String title;
    private final String artist;
    private final String album;
    private final String duration;
    private final String thumbnailUrl;
    private final String genre;
    private final String channel;
    private final String uploadDate;
    private final String googleDriveFileId;

    public MediaFile(UUID id, UUID jobId, String title, String artist, String album,
                     String duration, String thumbnailUrl, String genre, String channel,
                     String uploadDate, String googleDriveFileId) {
        this.id = id;
        this.jobId = jobId;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.thumbnailUrl = thumbnailUrl;
        this.genre = genre;
        this.channel = channel;
        this.uploadDate = uploadDate;
        this.googleDriveFileId = googleDriveFileId;
    }

    public UUID getId() { return id; }
    public UUID getJobId() { return jobId; }
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public String getAlbum() { return album; }
    public String getDuration() { return duration; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public String getGenre() { return genre; }
    public String getChannel() { return channel; }
    public String getUploadDate() { return uploadDate; }
    public String getGoogleDriveFileId() { return googleDriveFileId; }
}
