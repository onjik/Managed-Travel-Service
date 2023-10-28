CREATE TYPE gender AS ENUM (
    'MALE', 'FEMALE'
    );

CREATE TABLE user_account
(
    account_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(320) NOT NULL,
    roles TEXT[] NOT NULL ,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    gender GENDER NOT NULL ,
    birth_date DATE NOT NULL ,
    picture_uri TEXT DEFAULT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT unique_email UNIQUE (email)
);
