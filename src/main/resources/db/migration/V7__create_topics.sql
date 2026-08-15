CREATE TABLE IF NOT EXISTS topics (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    description TEXT,
    module_id INT,
    created TIMESTAMP,
    updated TIMESTAMP,
    FOREIGN KEY (module_id) REFERENCES module(id)
);