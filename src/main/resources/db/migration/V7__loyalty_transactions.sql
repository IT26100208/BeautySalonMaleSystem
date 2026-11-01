CREATE TABLE loyalty_transactions (
    id BIGINT NOT NULL AUTO_INCREMENT,
    customer_id BIGINT NOT NULL,
    points INT NOT NULL,
    description VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX idx_loyalty_customer_created (customer_id, created_at),
    CONSTRAINT fk_loyalty_customer
        FOREIGN KEY (customer_id) REFERENCES users (id)
);
