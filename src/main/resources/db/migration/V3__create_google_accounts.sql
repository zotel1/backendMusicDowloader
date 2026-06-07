CREATE TABLE google_accounts (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE,
    google_id VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL,
    access_token TEXT NOT NULL,
    refresh_token TEXT,
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_google_accounts_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX idx_google_accounts_user_id ON google_accounts(user_id);
CREATE INDEX idx_google_accounts_google_id ON google_accounts(google_id);
