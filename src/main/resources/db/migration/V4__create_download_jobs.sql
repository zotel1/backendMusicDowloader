CREATE TABLE download_jobs (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    type VARCHAR(20) NOT NULL,
    progress INT NOT NULL DEFAULT 0,
    source_url TEXT NOT NULL,
    error_message TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_download_jobs_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX idx_download_jobs_user_id ON download_jobs(user_id);
CREATE INDEX idx_download_jobs_status ON download_jobs(status);
