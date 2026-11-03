-- V12: Align staff table with Staff.java
-- The existing staff table has been confirmed empty.

ALTER TABLE staff
    CHANGE COLUMN id staff_id BIGINT NOT NULL AUTO_INCREMENT,
    CHANGE COLUMN first_name name VARCHAR(255),
    CHANGE COLUMN phone_number phone VARCHAR(50),
    CHANGE COLUMN role position VARCHAR(50),
    ADD COLUMN salary DECIMAL(12, 2) NULL;

-- Remove the unused columns from the old V8 schema.
ALTER TABLE staff
    DROP COLUMN last_name,
    DROP COLUMN specialization;
