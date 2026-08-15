CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    email VARCHAR(100),
    password VARCHAR(255),
    role_id INT,
    created TIMESTAMP,
    updated TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES roles(id)
);