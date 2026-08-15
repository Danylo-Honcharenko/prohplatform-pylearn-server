CREATE TABLE IF NOT EXISTS exercises (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    description TEXT,
    assessment INT,
    topic_id INT,
    created TIMESTAMP,
    updated TIMESTAMP,
    FOREIGN KEY (topic_id) REFERENCES topics(id)
);