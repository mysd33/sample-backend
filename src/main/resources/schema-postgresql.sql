/* Todoリスト */
CREATE TABLE IF NOT EXISTS todo (
    todo_id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL, -- この例ではDB独立したサービスとするため参照制約はつけない
    todo_title VARCHAR(60) NOT NULL,
    finished BOOLEAN,
    created_at TIMESTAMP
);

CREATE INDEX IF NOT EXISTS  todo_index_user_id ON todo(user_id);