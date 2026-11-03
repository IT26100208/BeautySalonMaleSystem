CREATE TABLE IF NOT EXISTS staff (
                                     staff_id BIGINT NOT NULL AUTO_INCREMENT,
                                     name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
    phone VARCHAR(20),
    position VARCHAR(50),
    salary DECIMAL(10,2),
    status VARCHAR(20) DEFAULT 'ACTIVE',
    PRIMARY KEY (staff_id)
    );
