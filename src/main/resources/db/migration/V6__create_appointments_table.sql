CREATE TABLE IF NOT EXISTS appointments
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    appointment_type VARCHAR(20)  NOT NULL,
    customer_email   VARCHAR(100) NOT NULL,
    customer_name    VARCHAR(100) NOT NULL,
    phone_number     VARCHAR(15)  NOT NULL,
    service_type     VARCHAR(40)  NOT NULL,
    stylist          VARCHAR(60)  NOT NULL,
    appointment_date DATE         NOT NULL,
    appointment_time TIME         NOT NULL,
    notes            VARCHAR(255),
    status           VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
    reschedule_count INT          NOT NULL DEFAULT 0,
    created_at       DATETIME     NOT NULL,
    updated_at       DATETIME     NOT NULL
);

CREATE INDEX idx_appointments_customer ON appointments (customer_email);
CREATE INDEX idx_appointments_stylist_date ON appointments (stylist, appointment_date);
