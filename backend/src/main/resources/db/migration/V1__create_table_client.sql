CREATE TABLE client (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    date_of_birth DATE,
    email VARCHAR(255) UNIQUE NOT NULL,
    document_number VARCHAR(50) UNIQUE NOT NULL
);