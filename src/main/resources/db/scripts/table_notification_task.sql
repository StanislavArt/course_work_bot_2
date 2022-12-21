-- liquibase formatted sql
-- changeset stanislav:1
CREATE TABLE notification_task (
    id SERIAL PRIMARY KEY,
    chat_id BIGINT,
    message TEXT,
    datetime_plan TIMESTAMP
);

-- changeset stanislav:2
ALTER TABLE  notification_task
ADD COLUMN sent BOOLEAN DEFAULT false;