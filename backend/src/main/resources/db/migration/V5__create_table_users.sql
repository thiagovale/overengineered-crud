CREATE TABLE users(
    id serial PRIMARY KEY,
    username varchar(255),
    password varchar(255)
);

ALTER TABLE logs ADD COLUMN user_id VARCHAR(255);
CREATE INDEX idx_user_id ON logs(user_id);