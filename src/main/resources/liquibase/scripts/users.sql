-- liquibase formatted sql

-- changeset tuzlukov:36
CREATE TABLE users
(
    id                 BIGSERIAL PRIMARY KEY,
    user_name          text,
    chat_id            BIGINT,
    start_registration BOOLEAN,
    start_report       BOOLEAN,
    have_warning       BOOLEAN
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

CREATE TABLE ownership_dogs
(
    id        BIGSERIAL PRIMARY KEY,
    owner_id  BIGSERIAL REFERENCES users_dog (id),
    animal_id BIGSERIAL REFERENCES animals_dog (id)
);

CREATE TABLE ownership_cats
(
    id        BIGSERIAL PRIMARY KEY,
    owner_id  BIGSERIAL REFERENCES users_cat (id),
    animal_id BIGSERIAL REFERENCES animals_cat (id)
);

CREATE TABLE users_reports
(
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGSERIAL REFERENCES users (id),
    report_date DATE,
    report_text TEXT
);

CREATE TABLE users_reports_photo
(
    id                      BIGSERIAL PRIMARY KEY,
    report_photo_filepath   TEXT,
    report_photo_filesize   BIGINT,
    report_photo_media_type TEXT,
    user_report_id          BIGSERIAL REFERENCES users_reports (id)
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