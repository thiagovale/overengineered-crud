CREATE TABLE logs (
    id SERIAL PRIMARY KEY,
    trace_id VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    method VARCHAR(10),
    path VARCHAR(500),
    status_code INTEGER,
    request_body TEXT,
    response_body TEXT,
    duration_ms INT,
    timestamp TIMESTAMP NOT NULL
);

CREATE INDEX idx_logs_trace_id ON logs (trace_id);
CREATE INDEX idx_logs_type ON logs (type);
CREATE INDEX idx_logs_timestamp ON logs (timestamp);