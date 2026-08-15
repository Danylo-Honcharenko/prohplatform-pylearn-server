CREATE TABLE IF NOT EXISTS test_result (
    id SERIAL PRIMARY KEY,
    test_uuid VARCHAR(255),
    user_id INT,
    max_assessment INT,
    assessment INT,
    correct JSONB,
    incorrect JSONB,
    created TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);