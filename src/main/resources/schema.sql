CREATE TABLE IF NOT EXISTS users
(
    id       bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email    varchar(255) NOT NULL,
    login    varchar(255) NOT NULL UNIQUE,
    name     varchar(255),
    birthday timestamp NOT NULL
);

CREATE TABLE IF NOT EXISTS user_friend
(
    user_id   bigint REFERENCES users (id) on delete cascade,
    friend_id bigint REFERENCES users (id) on delete cascade,
    PRIMARY KEY (user_id, friend_id)
);

CREATE TABLE IF NOT EXISTS rating
(
    id   bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name varchar(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS films
(
    id          bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        varchar(255) NOT NULL,
    description varchar(200),
    releaseDate timestamp,
    duration    int,
    rating_id   bigint REFERENCES rating (id)
);

CREATE TABLE IF NOT EXISTS film_like
(
    user_id bigint REFERENCES users (id) on delete cascade,
    film_id bigint REFERENCES films (id) on delete cascade,
    PRIMARY KEY (user_id, film_id)
);

CREATE TABLE IF NOT EXISTS genre
(
    id   bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name varchar(255) NOT NULL
);


CREATE TABLE IF NOT EXISTS film_genre
(
    genre_id bigint REFERENCES genre (id) on delete cascade,
    film_id  bigint REFERENCES films (id) on delete cascade,
    PRIMARY KEY (genre_id, film_id)
);