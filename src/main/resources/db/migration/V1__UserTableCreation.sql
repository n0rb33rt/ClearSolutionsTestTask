CREATE TABLE users
(
    id         BIGSERIAL,
    email      VARCHAR(319) NOT NULL,
    first_name VARCHAR(30) NOT NULL,
    last_name  VARCHAR(30) NOT NULL,
    birth_date DATE        NOT NULL,
    address    VARCHAR(60),
    phone      VARCHAR(14),
    PRIMARY KEY(id),
    UNIQUE(email),
    CONSTRAINT valid_email CHECK (email ~* '^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$'),
    CONSTRAINT valid_phone CHECK (phone IS NULL OR phone ~ '^\+380\d{9}$')

);
