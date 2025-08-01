CREATE TABLE categories (
	id BIGINT NOT NULL,
	created_at BIGINT NOT NULL,
	sort_key INT NOT NULL,
	title VARCHAR(120) NOT NULL,
	`order` VARCHAR(16) NOT NULL,
	user_id BIGINT NOT NULL,
	track TINYINT(1) NOT NULL,
	show_in_lib TINYINT(1) NOT NULL,
	deleted_at BIGINT,
	PRIMARY KEY (id, user_id),
	CONSTRAINT fk_categories_user_id
		FOREIGN KEY (user_id) REFERENCES users(id)
		ON DELETE CASCADE
);

CREATE TABLE favourites (
	manga_id BIGINT NOT NULL,
	category_id BIGINT NOT NULL,
	sort_key INT NOT NULL,
	pinned TINYINT(1) NOT NULL,
	created_at BIGINT NOT NULL,
	deleted_at BIGINT NOT NULL,
	user_id BIGINT NOT NULL,
	PRIMARY KEY (manga_id, category_id, user_id),
	INDEX idx_favourites_user_id (user_id),
	CONSTRAINT fk_favourites_manga_id
		FOREIGN KEY (manga_id) REFERENCES manga(id),
	CONSTRAINT fk_favourites_user_id
		FOREIGN KEY (user_id) REFERENCES users(id)
		ON DELETE CASCADE
);

CREATE TABLE history (
	manga_id BIGINT NOT NULL,
	created_at BIGINT NOT NULL,
	updated_at BIGINT NOT NULL,
	chapter_id BIGINT NOT NULL,
	page SMALLINT NOT NULL,
	scroll DOUBLE NOT NULL,
	percent DOUBLE NOT NULL,
	chapters INT NOT NULL,
	deleted_at BIGINT NOT NULL,
	user_id BIGINT NOT NULL,
	PRIMARY KEY (user_id, manga_id),
	INDEX idx_manga_id (manga_id),
	CONSTRAINT fk_history_manga_id
		FOREIGN KEY (manga_id) REFERENCES manga(id),
	CONSTRAINT fk_history_user_id
		FOREIGN KEY (user_id) REFERENCES users(id)
		ON DELETE CASCADE
);
