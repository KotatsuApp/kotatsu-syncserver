ALTER TABLE favourites
ADD CONSTRAINT fk_favourites_category
    FOREIGN KEY (category_id, user_id)
    REFERENCES categories (id, user_id)
    ON DELETE CASCADE;
