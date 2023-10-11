CREATE TYPE granted_role AS ENUM (
    'ROLE_ADMIN', 'ROLE_USER'
    );

CREATE TYPE gender AS ENUM (
    'MALE', 'FEMALE'
    );

CREATE TABLE user_account
(
    user_key BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(320) NOT NULL,
    roles GRANTED_ROLE[] NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    -- optional fields
    gender GENDER DEFAULT NULL,
    birth_date DATE DEFAULT NULL,
    picture_uri TEXT DEFAULT NULL,
    email_verified BOOLEAN DEFAULT NULL,
    CONSTRAINT unique_email UNIQUE (email)
);
