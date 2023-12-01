CREATE TABLE account_principal
(
    id BIGINT PRIMARY KEY
);

CREATE TABLE travel
(
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    owner_id BIGINT NOT NULL REFERENCES account_principal(id),
    plan JSON NOT NULL
)
