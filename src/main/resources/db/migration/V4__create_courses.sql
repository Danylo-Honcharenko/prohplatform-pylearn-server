CREATE TABLE IF NOT EXISTS courses (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    description TEXT,
    created TIMESTAMP,
    updated TIMESTAMP
);