CREATE TABLE playlists (
    id UUID PRIMARY KEY,
    owner_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    url TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_playlists_owner FOREIGN KEY (owner_id) REFERENCES users(id)
);

CREATE INDEX idx_playlists_owner_id ON playlists(owner_id);
