CREATE TABLE IF NOT EXISTS todo (
    todo_id VARCHAR(36) PRIMARY KEY,
    todo_title VARCHAR(30),
    finished BOOLEAN,
    created_at TIMESTAMP
);