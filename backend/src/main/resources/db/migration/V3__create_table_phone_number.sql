CREATE TABLE phone_number (
    id SERIAL PRIMARY KEY,
    number VARCHAR(20) NOT NULL,
    type VARCHAR(50) NOT NULL, -- 'HOME', 'MOBILE', 'WORK'

    client_id INT NOT NULL,
        CONSTRAINT fk_phone_client
            FOREIGN KEY (client_id)
            REFERENCES client (id)
);