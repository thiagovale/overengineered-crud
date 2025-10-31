CREATE TABLE address (
    id SERIAL PRIMARY KEY,
    street VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    zipcode VARCHAR(20) NOT NULL,

    client_id INT NOT NULL,
        CONSTRAINT fk_address_client
            FOREIGN KEY (client_id)
            REFERENCES client (id)
);