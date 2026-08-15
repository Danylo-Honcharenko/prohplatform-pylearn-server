CREATE TABLE IF NOT EXISTS auth (
    id SERIAL PRIMARY KEY,
    user_id INT,
    access_token VARCHAR(255),
    created TIMESTAMP,
    expires_in TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);