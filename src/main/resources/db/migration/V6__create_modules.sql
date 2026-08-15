CREATE TABLE IF NOT EXISTS module (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    description TEXT,
    course_id INT,
    created TIMESTAMP,
    updated TIMESTAMP,
    FOREIGN KEY (course_id) REFERENCES courses(id)
);