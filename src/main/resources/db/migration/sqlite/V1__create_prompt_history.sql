-- Create prompt_history table for SQLite
CREATE TABLE prompt_history (
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

-- Create index on prompt_type for faster queries
CREATE INDEX idx_prompt_type ON prompt_history(prompt_type);

-- Create index on created_at for date-based queries
CREATE INDEX idx_created_at ON prompt_history(created_at);

-- Create index on request_id for lookups
CREATE INDEX idx_request_id ON prompt_history(request_id);

-- Create index on status for filtering
CREATE INDEX idx_status ON prompt_history(status);
