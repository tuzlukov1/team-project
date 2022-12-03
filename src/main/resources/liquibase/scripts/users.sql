-- liquibase formatted sql

-- changeset tuzlukov:15
CREATE TABLE users
(
    id        SERIAL PRIMARY KEY,
    user_name text,
    chat_id   BIGINT
);

CREATE TABLE users_dog
(
    id        SERIAL PRIMARY KEY,
    user_id   SERIAL REFERENCES users (id),
    full_name TEXT,
    phone     BIGINT
);

CREATE TABLE users_cat
(
    id        SERIAL PRIMARY KEY,
    user_id   SERIAL REFERENCES users (id),
    full_name TEXT,
    phone     BIGINT
);

CREATE TABLE animals_dog
(
    id    SERIAL PRIMARY KEY,
    name  TEXT,
    breed TEXT,
    age   INTEGER
);

CREATE TABLE animals_cat
(
    id    SERIAL PRIMARY KEY,
    name  TEXT,
    breed TEXT,
    age   INTEGER
);

CREATE TABLE animal_avatars_dogs
(
    id         SERIAL PRIMARY KEY,
    filepath   TEXT,
    filesize   BIGINT,
    media_type TEXT
);

CREATE TABLE animal_avatars_cats
(
    id         SERIAL PRIMARY KEY,
    filepath   TEXT,
    filesize   BIGINT,
    media_type TEXT
);

CREATE TABLE animal_avatars_connection_dogs
(
    id        SERIAL PRIMARY KEY,
    animal_id SERIAL REFERENCES animals_dog (id),
    avatar_id SERIAL REFERENCES animal_avatars_dogs (id)
);

CREATE TABLE animal_avatars_connection_cats
(
    id        SERIAL PRIMARY KEY,
    animal_id SERIAL REFERENCES animals_cat (id),
    avatar_id SERIAL REFERENCES animal_avatars_cats (id)
);

CREATE TABLE ownership_dogs
(
    id        SERIAL PRIMARY KEY,
    owner_id  SERIAL REFERENCES users_dog (id),
    animal_id SERIAL REFERENCES animals_dog (id)
);

CREATE TABLE ownership_cats
(
    id        SERIAL PRIMARY KEY,
    owner_id  SERIAL REFERENCES users_cat (id),
    animal_id SERIAL REFERENCES animals_cat (id)
);