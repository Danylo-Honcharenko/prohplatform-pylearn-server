CREATE TABLE IF NOT EXISTS module_stat (
    id SERIAL PRIMARY KEY,
    module_id INT,
    topic_id INT,
    user_id INT,
    FOREIGN KEY (module_id) REFERENCES module(id),
    FOREIGN KEY (topic_id) REFERENCES topics(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);