CREATE DATABASE travel_service;

CREATE TABLE user_profile
(
    user_key BIGSERIAL PRIMARY KEY,
    email    VARCHAR(320) NOT NULL,
    name     VARCHAR(10) DEFAULT NULL,
    CONSTRAINT unique_email UNIQUE (email)
);


CREATE TYPE gender AS ENUM('W','N');
-- CREATE TYPE role AS VARCHAR(255);
CREATE TABLE user_detail
(
    user_key BIGINT PRIMARY KEY,
    gender GENDER NOT NULL ,
    birth_date DATE NOT NULL ,
    create_date TIMESTAMP NOT NULL DEFAULT current_timestamp,
    FOREIGN KEY (user_key) REFERENCES user_profile (user_key)
);

CREATE TABLE activation_key
(
    user_key BIGINT PRIMARY KEY ,
    activation_key UUID NOT NULL ,
    created_at TIMESTAMP NOT NULL DEFAULT current_timestamp,
    FOREIGN KEY (user_key) REFERENCES user_profile(user_key),
    UNIQUE (activation_key)
);

CREATE TABLE granted_role
(
    role_key  SMALLSERIAL PRIMARY KEY,
    role_name varchar(255) NOT NULL,
    UNIQUE (role_name)
);

CREATE TABLE user_role
(
    user_key BIGINT ,
    role_key SMALLINT,
    PRIMARY KEY (user_key, role_key)
);

CREATE TABLE security_issue
(
    security_issue_key UUID PRIMARY KEY,
    user_key BIGINT NOT NULL ,
    judgement_date TIMESTAMP NOT NULL DEFAULT current_timestamp,
    event_details TEXT DEFAULT NULL,
    issue_type CHAR(6) NOT NULL,
    FOREIGN KEY (user_key) REFERENCES user_profile(user_key)
);

CREATE INDEX ON security_issue(user_key);

CREATE TABLE account_locked
(
    security_issue_key UUID PRIMARY KEY ,
    expiration_date TIMESTAMP NULL DEFAULT NULL,
    reason TEXT NULL DEFAULT NULL,
    FOREIGN KEY (security_issue_key) REFERENCES security_issue(security_issue_key)
);

CREATE TABLE account_disabled
(
    security_issue_key UUID PRIMARY KEY ,
    is_resolved BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (security_issue_key) REFERENCES security_issue(security_issue_key)
);

CREATE INDEX ON account_disabled(is_resolved);

CREATE TABLE account_expired
(
    security_issue_key UUID PRIMARY KEY,
    last_login_time TIMESTAMP NOT NULL ,
    is_resolved BOOL NOT NULL DEFAULT FALSE,
    FOREIGN KEY (security_issue_key) REFERENCES security_issue(security_issue_key)
);

CREATE INDEX ON account_expired(is_resolved);


CREATE TABLE blocked_subnet
(
    subnet CIDR PRIMARY KEY,
    since TIMESTAMP NOT NULL DEFAULT current_timestamp,
    until TIMESTAMP NOT NULL
);

CREATE INDEX ON blocked_subnet(until);

CREATE TABLE authentication
(
    auth_key BIGSERIAL PRIMARY KEY,
    auth_type CHAR(6) NOT NULL,
    user_key BIGINT NOT NULL ,
    FOREIGN KEY (user_key) REFERENCES user_profile(user_key)
);

CREATE INDEX ON authentication (user_key);

CREATE TABLE auth_2_provider
(
    registration_id VARCHAR(20) PRIMARY KEY ,
    client_name VARCHAR(150) NOT NULL ,
    client_secret TEXT NOT NULL ,
    authentication_method VARCHAR(100) NOT NULL ,
    authorization_grant_type VARCHAR(20) NOT NULL ,
    redirect_uri TEXT NOT NULL ,
    username_attr_name VARCHAR(50) NOT NULL ,
    client_id VARCHAR(255) NOT NULL ,
    scopes TEXT[] NOT NULL ,
    UNIQUE (registration_id, client_id)
);



CREATE TABLE auth_2
(
    auth_key BIGINT PRIMARY KEY ,
    registration_id VARCHAR(20) NOT NULL ,
    FOREIGN KEY (auth_key) REFERENCES authentication(auth_key),
    FOREIGN KEY (registration_id) REFERENCES auth_2_provider(registration_id)
);

CREATE TABLE provider_detail
(
    registration_id VARCHAR(20) PRIMARY KEY ,
    authorization_uri TEXT NOT NULL ,
    token_uri TEXT NOT NULL ,
    user_info_uri TEXT NOT NULL ,
    jwk_set_uri TEXT NOT NULL ,
    issuer_uri TEXT NOT NULL ,
    configuration_metadata JSONB NOT NULL ,
    FOREIGN KEY (registration_id) REFERENCES auth_2_provider(registration_id)
);

INSERT INTO granted_role(role_name) VALUES ('ROLE_ADMIN');
INSERT INTO granted_role(role_name) VALUES ('ROLE_USER');
INSERT INTO granted_role(role_name) VALUES ('ROLE_ANONYMOUS');
INSERT INTO granted_role(role_name) VALUES ('ROLE_SPECIAL_EXPIRED_ACCOUNT');
INSERT INTO granted_role(role_name) VALUES ('ROLE_SPECIAL_INCOMPLETE');