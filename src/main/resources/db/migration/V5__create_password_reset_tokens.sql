ALTER TABLE users
    ADD COLUMN password_reset_token_hash CHAR(64),
    ADD COLUMN password_reset_token_expires_at BIGINT,
    ADD UNIQUE INDEX uq_users_password_reset_token_hash (password_reset_token_hash);
