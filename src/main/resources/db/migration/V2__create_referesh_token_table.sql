CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE refresh_tokens(

    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    user_id UUID NOT NULL ,
    token VARCHAR(512) NOT NULL ,
    device_name VARCHAR(50),
    user_agent VARCHAR(512),
    ip_address VARCHAR(50),
    expires_at TIMESTAMPTZ NOT NULL ,

    revoked BOOLEAN NOT NULL DEFAULT FALSE,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_refresh_tokens_user
        FOREIGN KEY (user_id)
            REFERENCES users(id)
            ON DELETE CASCADE,

    CONSTRAINT uk_refresh_tokens_token
        UNIQUE (token)
);