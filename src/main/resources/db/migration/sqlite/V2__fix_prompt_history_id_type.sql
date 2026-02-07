-- Fix prompt_history id column type from INTEGER to BIGINT
-- SQLite doesn't support ALTER COLUMN, so we need to recreate the table

-- Create new table with correct schema
CREATE TABLE prompt_history_new (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    request_id TEXT UNIQUE NOT NULL,
    prompt_type TEXT NOT NULL,
    job_description TEXT,
    company TEXT,
    job_title TEXT,
    interviewer_name TEXT,
    temperature REAL DEFAULT 0.7,
    model TEXT,
    expanded_prompt_json TEXT,
    generated_content TEXT,
    generated_file_path TEXT,
    output_format TEXT DEFAULT 'markdown',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    file_size_bytes INTEGER,
    llm_response_time_ms INTEGER,
    token_usage_estimate INTEGER,
    status TEXT DEFAULT 'completed',
    error_message TEXT
);

-- Copy data from old table to new table
INSERT INTO prompt_history_new
SELECT * FROM prompt_history;

-- Drop old table
DROP TABLE prompt_history;

-- Rename new table to original name
ALTER TABLE prompt_history_new RENAME TO prompt_history;

-- Recreate indexes
CREATE INDEX idx_prompt_type ON prompt_history(prompt_type);
CREATE INDEX idx_created_at ON prompt_history(created_at);
