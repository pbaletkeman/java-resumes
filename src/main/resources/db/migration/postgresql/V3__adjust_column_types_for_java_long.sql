-- PostgreSQL migration V3 - Adjust column types to match Java Long types
-- This fixes the mismatch between INTEGER (4 bytes) and BIGINT (8 bytes)

ALTER TABLE prompt_history
  ALTER COLUMN file_size_bytes TYPE BIGINT,
  ALTER COLUMN llm_response_time_ms TYPE BIGINT;
