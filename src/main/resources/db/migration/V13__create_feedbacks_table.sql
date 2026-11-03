-- Create table for customer feedback.
-- Only use if feedbacks does not already exist.

CREATE TABLE feedbacks (
    id BIGINT NOT NULL AUTO_INCREMENT,

    customer_email VARCHAR(255),
    customer_name VARCHAR(255),

    comment VARCHAR(1000),
    rating INT NOT NULL,

    created_at DATETIME,

    PRIMARY KEY (id)
);
