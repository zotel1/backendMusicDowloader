package com.principal.backend.infrastructure.adapter.jpa.entity;

import com.principal.backend.domain.model.MediaFile;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "media_files")
public class MediaFileEntity {
    @Id private UUID id;
    @Column(name = "job_id", nullable = false) private UUID jobId;
    private String title; private String artist; private String album; private String duration;
    @Column(name = "thumbnail_url") private String thumbnailUrl;
    private String genre; private String channel;
    @Column(name = "upload_date") private String uploadDate;
    @Column(name = "google_drive_file_id") private String googleDriveFileId;

    public MediaFileEntity() {}

    public static MediaFileEntity fromDomain(MediaFile mf) {
        MediaFileEntity e = new MediaFileEntity();
        e.id = mf.getId(); e.jobId = mf.getJobId(); e.title = mf.getTitle(); e.artist = mf.getArtist();
        e.album = mf.getAlbum(); e.duration = mf.getDuration(); e.thumbnailUrl = mf.getThumbnailUrl();
        e.genre = mf.getGenre(); e.channel = mf.getChannel(); e.uploadDate = mf.getUploadDate();
        e.googleDriveFileId = mf.getGoogleDriveFileId();
        return e;
    }
    public MediaFile toDomain() {
        return new MediaFile(id, jobId, title, artist, album, duration, thumbnailUrl, genre, channel, uploadDate, googleDriveFileId);
    }

    public UUID getId() { return id; } public void setId(UUID id) { this.id = id; }
    public UUID getJobId() { return jobId; } public void setJobId(UUID jobId) { this.jobId = jobId; }
    public String getTitle() { return title; } public void setTitle(String title) { this.title = title; }
    public String getArtist() { return artist; } public void setArtist(String artist) { this.artist = artist; }
    public String getAlbum() { return album; } public void setAlbum(String album) { this.album = album; }
    public String getDuration() { return duration; } public void setDuration(String duration) { this.duration = duration; }
    public String getThumbnailUrl() { return thumbnailUrl; } public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }
    public String getGenre() { return genre; } public void setGenre(String genre) { this.genre = genre; }
    public String getChannel() { return channel; } public void setChannel(String channel) { this.channel = channel; }
    public String getUploadDate() { return uploadDate; } public void setUploadDate(String uploadDate) { this.uploadDate = uploadDate; }
    public String getGoogleDriveFileId() { return googleDriveFileId; } public void setGoogleDriveFileId(String googleDriveFileId) { this.googleDriveFileId = googleDriveFileId; }
}
