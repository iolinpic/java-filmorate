CREATE TABLE IF NOT EXISTS users
(
    id       bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email    varchar,
    login    varchar,
    name     varchar,
    birthday timestamp
);

CREATE TABLE IF NOT EXISTS user_friend
(
    user_id   bigint,
    friend_id bigint,
    PRIMARY KEY (user_id, friend_id)
);

CREATE TABLE IF NOT EXISTS films
(
    id          bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        varchar,
    description text,
    releaseDate timestamp,
    duration    int,
    rating_id   bigint
);

CREATE TABLE IF NOT EXISTS film_like
(
    user_id bigint,
    film_id bigint,
    PRIMARY KEY (user_id, film_id)
);

CREATE TABLE IF NOT EXISTS genre
(
    id  bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name varchar
);

CREATE TABLE IF NOT EXISTS rating
(
    id   bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name varchar
);

CREATE TABLE IF NOT EXISTS film_genre
(
    genre_id bigint,
    film_id  bigint,
    PRIMARY KEY (genre_id, film_id)
);

ALTER TABLE film_like
    ADD CONSTRAINT user_like FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE film_like
    ADD CONSTRAINT film_like FOREIGN KEY (film_id) REFERENCES films (id);

ALTER TABLE user_friend
    ADD CONSTRAINT user_friend FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE user_friend
    ADD CONSTRAINT friend_user FOREIGN KEY (friend_id) REFERENCES users (id);

ALTER TABLE films
    ADD CONSTRAINT film_rating FOREIGN KEY (rating_id) REFERENCES rating (id);

ALTER TABLE film_genre
    ADD FOREIGN KEY (genre_id) REFERENCES genre (id);

ALTER TABLE film_genre
    ADD FOREIGN KEY (film_id) REFERENCES films (id);
