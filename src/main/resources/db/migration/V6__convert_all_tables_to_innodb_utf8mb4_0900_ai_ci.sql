SET foreign_key_checks = 0;

ALTER TABLE users
    CONVERT TO CHARSET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
    ENGINE = InnoDB;

ALTER TABLE manga
    CONVERT TO CHARSET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
    ENGINE = InnoDB;

ALTER TABLE tags
    CONVERT TO CHARSET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
    ENGINE = InnoDB;

ALTER TABLE manga_tags
    CONVERT TO CHARSET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
    ENGINE = InnoDB;

ALTER TABLE categories
    CONVERT TO CHARSET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
    ENGINE = InnoDB;

ALTER TABLE favourites
    CONVERT TO CHARSET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
    ENGINE = InnoDB;

ALTER TABLE history
    CONVERT TO CHARSET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
    ENGINE = InnoDB;

SET foreign_key_checks = 1;
