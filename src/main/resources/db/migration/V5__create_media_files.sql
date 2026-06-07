CREATE TABLE media_files (
    id UUID PRIMARY KEY,
    job_id UUID NOT NULL,
    title VARCHAR(512),
    artist VARCHAR(255),
    album VARCHAR(255),
    duration VARCHAR(20),
    thumbnail_url TEXT,
    genre VARCHAR(100),
    channel VARCHAR(255),
    upload_date VARCHAR(20),
    google_drive_file_id VARCHAR(255),
    CONSTRAINT fk_media_files_job FOREIGN KEY (job_id) REFERENCES download_jobs(id)
);

CREATE INDEX idx_media_files_job_id ON media_files(job_id);
