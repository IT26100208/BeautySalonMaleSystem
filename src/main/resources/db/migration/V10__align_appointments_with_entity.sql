
-- V10: Align appointments table with the
-- completed Appointment Management module.
--
-- Intended for the existing empty development
-- appointments table created by V6.
-- Do not apply to databases containing old bookings.

ALTER TABLE appointments
    DROP FOREIGN KEY fk_appointment_customer;

ALTER TABLE appointments
    DROP COLUMN customer_id,
    DROP COLUMN appointment_date_time,

    ADD COLUMN appointment_type VARCHAR(20)
        NOT NULL DEFAULT 'REGULAR' AFTER id,

    ADD COLUMN customer_email VARCHAR(100)
        NOT NULL AFTER appointment_type,

    ADD COLUMN customer_name VARCHAR(100)
        NOT NULL AFTER customer_email,

    ADD COLUMN phone_number VARCHAR(15)
        NOT NULL AFTER customer_name,

    MODIFY COLUMN service_type VARCHAR(40)
        NOT NULL,

    ADD COLUMN stylist VARCHAR(60)
        NOT NULL AFTER service_type,

    ADD COLUMN appointment_date DATE
        NOT NULL AFTER stylist,

    ADD COLUMN appointment_time TIME
        NOT NULL AFTER appointment_date,

    ADD COLUMN notes VARCHAR(255)
        NULL AFTER appointment_time,

    MODIFY COLUMN status VARCHAR(20)
        NOT NULL DEFAULT 'PENDING',

    ADD COLUMN reschedule_count INT
        NOT NULL DEFAULT 0 AFTER status,

    MODIFY COLUMN created_at DATETIME
        NOT NULL DEFAULT CURRENT_TIMESTAMP,

    ADD COLUMN updated_at DATETIME
        NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP;
