CREATE TABLE IF NOT EXISTS answers_to_exercise (
    id SERIAL PRIMARY KEY,
    text TEXT,
    file_path VARCHAR(255),
    assessment INT,
    user_id INT,
    created TIMESTAMP,
    updated TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);