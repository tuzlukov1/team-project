-- liquibase formatted sql

-- changeset tuzlukov:6
CREATE TABLE users
(
    id          SERIAL PRIMARY KEY,
    name        TEXT,
    chat_id     BIGINT,
    have_animal BOOLEAN
);

CREATE TABLE animals
(
    id        SERIAL PRIMARY KEY,
    name      TEXT,
    breed      TEXT,
    age      INTEGER
);

CREATE TABLE animal_avatars
(
    id         SERIAL PRIMARY KEY,
    filepath   TEXT,
    filesize   BIGINT,
    media_type TEXT
);

CREATE TABLE animal_avatars_connection
(
    id        SERIAL PRIMARY KEY,
    animal_id SERIAL REFERENCES animals (id),
    avatar_id SERIAL REFERENCES animal_avatars (id)
);

CREATE TABLE ownership
(
    id        SERIAL PRIMARY KEY,
    owner_id  SERIAL REFERENCES users (id),
    animal_id SERIAL REFERENCES animals (id)
)