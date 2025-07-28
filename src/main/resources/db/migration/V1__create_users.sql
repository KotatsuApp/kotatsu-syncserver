CREATE TABLE users (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	email VARCHAR(320) NOT NULL,
	password_hash VARCHAR(128) NOT NULL,
	nickname VARCHAR(100),
	favourites_sync_timestamp BIGINT,
	history_sync_timestamp BIGINT,
	CONSTRAINT uq_users_email UNIQUE (email)
);

--DROP TABLE IF EXISTS users;
