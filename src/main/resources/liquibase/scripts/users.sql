-- liquibase formatted sql

-- changeset tuzlukov:15
CREATE TABLE users
(
    id        BIGSERIAL PRIMARY KEY,
    user_name text,
    chat_id   BIGINT
);

CREATE TABLE users_dog
(
    id        BIGSERIAL PRIMARY KEY,
    user_id   BIGSERIAL REFERENCES users (id),
    full_name TEXT,
    phone     BIGINT
);

CREATE TABLE users_cat
(
    id        BIGSERIAL PRIMARY KEY,
    user_id   BIGSERIAL REFERENCES users (id),
    full_name TEXT,
    phone     BIGINT
);

CREATE TABLE animals_dog
(
    id    BIGSERIAL PRIMARY KEY,
    name  TEXT,
    breed TEXT,
    age   INTEGER
);

CREATE TABLE animals_cat
(
    id    BIGSERIAL PRIMARY KEY,
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
    id        BIGSERIAL PRIMARY KEY,
    owner_id  BIGSERIAL REFERENCES users_dog (id),
    animal_id BIGSERIAL REFERENCES animals_dog (id)
);

CREATE TABLE ownership_cats
(
    id                 BIGSERIAL PRIMARY KEY,
    owner_id           BIGSERIAL REFERENCES users_cat (id),
    animal_id          BIGSERIAL REFERENCES animals_cat (id)
);

-- changeSet shadrin:1
ALTER TABLE ownership_cats
    ADD COLUMN end_date_probation DATE;

ALTER TABLE ownership_cats
    ADD COLUMN probation_days INTEGER;

ALTER TABLE ownership_cats
    ADD COLUMN passage_probation TEXT DEFAULT 'не окончен';

-- changeSet shadrin:2
ALTER TABLE ownership_dogs
    ADD COLUMN end_date_probation DATE;

ALTER TABLE ownership_dogs
    ADD COLUMN probation_days INTEGER;

ALTER TABLE ownership_dogs
    ADD COLUMN passage_probation TEXT DEFAULT 'не окончен';