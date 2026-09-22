ALTER TABLE module_stat
ADD CONSTRAINT unique_topic_id_and_user_id UNIQUE (topic_id, user_id);