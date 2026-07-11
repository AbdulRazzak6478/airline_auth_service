

CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TYPE user_role AS ENUM ( 'ADMIN','SUPER_ADMIN','AIRLINE_MANAGER', 'STAFF', 'CUSTOMER','USER');

CREATE TYPE user_status AS ENUM ( 'ACTIVE', 'INACTIVE', 'SUSPENDED');

CREATE TABLE users(

    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    first_name VARCHAR(50) NOT NULL ,
    last_name VARCHAR(50) NOT NULL ,
    email VARCHAR(50) NOT NULL,
    mobile_number VARCHAR(20) NOT NULL ,
    role user_role NOT NULL ,
    password VARCHAR(255) NOT NULL ,
    status user_status NOT NULL DEFAULT 'ACTIVE',
    email_verified BOOLEAN DEFAULT FALSE,

    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_users_email UNIQUE (email),
    CONSTRAINT uk_users_mobile_number UNIQUE (mobile_number),

    CONSTRAINT chk_users_first_name
        CHECK (length(trim(first_name)) > 0),

    CONSTRAINT chk_users_last_name
        CHECK (length(trim(last_name)) > 0),

    CONSTRAINT chk_users_email
        CHECK (length(trim(email)) > 0),

    CONSTRAINT chk_users_mobile_number
        CHECK (length(trim(mobile_number)) > 0)
);