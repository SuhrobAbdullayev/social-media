CREATE TABLE if not exists posts (
                       id SERIAL PRIMARY KEY,
                       key VARCHAR(255),
                       text TEXT,
                       like_count INTEGER NOT NULL DEFAULT 0,
                       share_count INTEGER NOT NULL DEFAULT 0,
                       view_count INTEGER NOT NULL DEFAULT 0,
                       is_media BOOLEAN NOT NULL DEFAULT false,
                       is_blocked BOOLEAN NOT NULL DEFAULT false,
                       user_id BIGINT NOT NULL,
                        date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE if not exists comments (
                          id SERIAL PRIMARY KEY,
                          text TEXT,
                          like_count INTEGER NOT NULL DEFAULT 0,
                          post_id BIGINT NOT NULL,
                          user_id BIGINT NOT NULL,
                            date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS followers (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    follower_id BIGINT NOT NULL,
    date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE if not exists likes(
                                    id SERIAL PRIMARY KEY,
                                    post_id BIGINT NOT NULL,
                                    user_id BIGINT NOT NULL,
                                    date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);