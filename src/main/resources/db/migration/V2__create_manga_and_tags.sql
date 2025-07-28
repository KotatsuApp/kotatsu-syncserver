CREATE TABLE manga (
	id BIGINT PRIMARY KEY,
	title VARCHAR(255) NOT NULL,
	alt_title VARCHAR(255),
	url VARCHAR(255) NOT NULL,
	public_url VARCHAR(255) NOT NULL,
	rating FLOAT NOT NULL,
	content_rating ENUM('SAFE', 'SUGGESTIVE', 'ADULT'),
	cover_url VARCHAR(255) NOT NULL,
	large_cover_url VARCHAR(255),
	state ENUM('ONGOING', 'FINISHED', 'ABANDONED', 'PAUSED', 'UPCOMING', 'RESTRICTED'),
	author VARCHAR(64),
	source VARCHAR(32) NOT NULL
);

CREATE TABLE tags (
	id BIGINT PRIMARY KEY,
	title VARCHAR(64) NOT NULL,
	`key` VARCHAR(120) NOT NULL,
	source VARCHAR(32) NOT NULL
);

CREATE TABLE manga_tags (
	manga_id BIGINT NOT NULL,
	tag_id BIGINT NOT NULL,
	PRIMARY KEY (manga_id, tag_id),
	INDEX idx_manga_tags_tag_id (tag_id),
	CONSTRAINT fk_manga_tags_tag_id
		FOREIGN KEY (tag_id) REFERENCES tags(id),
	CONSTRAINT fk_manga_tags_manga_id
		FOREIGN KEY (manga_id) REFERENCES manga(id)
		ON DELETE CASCADE
);

--DROP TABLE IF EXISTS manga_tags;
--DROP TABLE IF EXISTS tags;
--DROP TABLE IF EXISTS manga;
