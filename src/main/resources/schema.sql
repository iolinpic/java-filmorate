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
    user_id   bigint REFERENCES users (id),
    friend_id bigint REFERENCES users (id),
    PRIMARY KEY (user_id, friend_id)
);

CREATE TABLE IF NOT EXISTS rating
(
    id   bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name varchar
);

CREATE TABLE IF NOT EXISTS films
(
    id          bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        varchar,
    description text,
    releaseDate timestamp,
    duration    int,
    rating_id   bigint REFERENCES rating (id)
);

CREATE TABLE IF NOT EXISTS film_like
(
    user_id bigint REFERENCES users (id),
    film_id bigint REFERENCES films (id),
    PRIMARY KEY (user_id, film_id)
);

CREATE TABLE IF NOT EXISTS genre
(
    id   bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name varchar
);


CREATE TABLE IF NOT EXISTS film_genre
(
    genre_id bigint REFERENCES genre (id),
    film_id  bigint REFERENCES films (id),
    PRIMARY KEY (genre_id, film_id)
);