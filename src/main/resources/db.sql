CREATE TABLE IF NOT EXISTS `roles` (
    `id` SERIAL PRIMARY KEY,
    `name` VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS `users` (
    `id` SERIAL PRIMARY KEY,
    `first_name` VARCHAR(100),
    `last_name` VARCHAR(100),
    `email` VARCHAR(100),
    `password` VARCHAR(255),
    `role_id` INT,
    `created` TIMESTAMP,
    `updated` TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE IF NOT EXISTS `courses` (
    `id` SERIAL PRIMARY KEY,
    `name` VARCHAR(100),
    `description` TEXT,
    `created` TIMESTAMP,
    `updated` TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `user_course` (
    `user_id` INT,
    `course_id` INT,
    FOREIGN KEY (user_id) REFERENCES `users`(id),
    FOREIGN KEY (course_id) REFERENCES courses(id)
);

CREATE TABLE IF NOT EXISTS `module` (
    `id` SERIAL PRIMARY KEY,
    `name` VARCHAR(100),
    `description` TEXT,
    `course_id` INT,
    `created` TIMESTAMP,
    `updated` TIMESTAMP,
    FOREIGN KEY (course_id) REFERENCES courses(id)
);

CREATE TABLE IF NOT EXISTS `topics` (
    `id` SERIAL PRIMARY KEY,
    `name` VARCHAR(100),
    `description` TEXT,
    `module_id` INT,
    `created` TIMESTAMP,
    `updated` TIMESTAMP,
    FOREIGN KEY (module_id) REFERENCES module(id)
);

CREATE TABLE IF NOT EXISTS `exercises` (
    `id` SERIAL PRIMARY KEY,
    `name` VARCHAR(100),
    `description` TEXT,
    `assessment` INT,
    `topic_id` INT,
    `created` TIMESTAMP,
    `updated` TIMESTAMP,
    FOREIGN KEY (topic_id) REFERENCES topics(id)
);

CREATE TABLE IF NOT EXISTS `answers_to_exercise` (
    `id` SERIAL PRIMARY KEY,
    `text` TEXT,
    `file_path` VARCHAR(255),
    `assessment` INT,
    `user_id` INT,
    `created` TIMESTAMP,
    `updated` TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS `test_result` (
    `id` SERIAL PRIMARY KEY,
    `test_uuid` VARCHAR(255),
    `user_id` INT,
    `max_assessment` INT,
    `assessment` INT,
    `correct` JSONB,
    `incorrect` JSONB,
    `created` TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS `module_stat` (
    `id` SERIAL PRIMARY KEY,
    `module_id` INT,
    `topic_id` INT,
    `user_id` INT,
    FOREIGN KEY (module_id) REFERENCES module(id),
    FOREIGN KEY (topic_id) REFERENCES topics(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS `auth` (
    `id` SERIAL PRIMARY KEY,
    `user_id` INT,
    `access_token` VARCHAR(255),
    `created` TIMESTAMP,
    `expires_in` TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_TEACHER');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');

INSERT INTO users (first_name, last_name, email, password, role_id, created)
VALUES ('David', 'Menchuk', 'test.test@icloud.com','123456789', 1, '2024-09-11 17:09:10');

INSERT INTO users (first_name, last_name, email ,password, role_id, created)
VALUES ('Dima', 'Valentinov', 'test1.test@icloud.com', '123456789', 1, '2024-09-11 17:09:10');

INSERT INTO users (first_name, last_name, email ,password, role_id, created)
VALUES ('Sasha', 'Valentinov', 'testOper.test@icloud.com', '123456789', 1, '2024-09-11 17:09:10');

INSERT INTO users (first_name, last_name, email ,password, role_id, created)
VALUES ('Andry', 'Valente', 'testOper.test@icloud.com', '123456789', 1, '2024-09-11 17:09:10');

INSERT INTO users (first_name, last_name, password, role_id, created)
VALUES ('Valera', 'Valentinov', '123456789', 2, '2024-09-11 17:09:10');

INSERT INTO courses (name, description, created)
VALUES ('C++', 'This is...', '2024-8-05 12:10:44');

INSERT INTO courses (name, description, created)
VALUES ('Python', 'This is...', '2024-8-05 12:10:44');

INSERT INTO courses (name, description, created)
VALUES ('Java', 'This is...', '2024-8-05 12:10:44');

INSERT INTO user_course (user_id, course_id)
VALUES (1, 1);

INSERT INTO user_course (user_id, course_id)
VALUES (1, 2);

INSERT INTO user_course (user_id, course_id)
VALUES (3, 1);

INSERT INTO module (name, description, course_id, created)
VALUES ('Module 1', 'This is...', 1,'2024-04-12 09:12:44');

INSERT INTO module (name, description, course_id, created)
VALUES ('Module 2', 'This is...', 1,'2024-04-12 09:12:44');

INSERT INTO topics (name, description, created, module_id)
VALUES ('Pointers', 'This is...', '2024-04-12 09:12:44', 1);

INSERT INTO topics (name, description, created, module_id)
VALUES ('Variable', 'This is...', '2024-04-12 09:12:44', 1);

INSERT INTO topics (name, description, created, module_id)
VALUES ('Variable 2', 'This is...', '2024-04-12 09:12:44', 1);

INSERT INTO exercises (name, description, created, topic_id)
VALUES ('Deploy APP', 'This is...', '2024-05-12 09:12:44', 1);

INSERT INTO exercises (name, description, created, topic_id)
VALUES ('Deploy APP 2', 'This is...', '2024-05-12 09:12:44', 2);

INSERT INTO test_result (test_uuid, user_id, max_assessment, assessment, correct, incorrect, created)
VALUES ('oioe79kd90s68n5793', 1, 100, 50, '{"test": "ok!"}', '{}','2024-04-12 09:12:44');

INSERT INTO test_result (test_uuid, user_id, max_assessment, assessment, correct, incorrect, created)
VALUES ('pppp79kd90s68n5793', 1, 100, 40, '{}', '{}','2024-04-12 09:12:44');

INSERT INTO module_stat (module_id, topic_id, user_id)
VALUES (1, 1, 1);

INSERT INTO module_stat (module_id, topic_id, user_id)
VALUES (1, 1, 1);

INSERT INTO auth (user_id, access_token, created, expires_in)
VALUES (1, '$123456#', '2024-04-12 09:12:44', '2024-04-12 09:12:44');

INSERT INTO auth (user_id, access_token, created, expires_in)
VALUES (1, '$123456#', '2024-04-12 09:12:44', '2024-04-12 09:12:44');