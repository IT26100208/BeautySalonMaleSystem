-- V11: Align the services table with SalonService.java
-- Preserve existing service records and the legacy status column.

ALTER TABLE services
    ADD COLUMN category VARCHAR(255)
        NOT NULL DEFAULT 'General',

    ADD COLUMN is_active BOOLEAN
        NOT NULL DEFAULT TRUE;

-- Preserve inactive status for existing services.
-- Old statuses not identified as inactive remain active.
UPDATE services
SET is_active = CASE
    WHEN UPPER(TRIM(status)) IN
         ('INACTIVE', 'DISABLED', 'UNAVAILABLE')
        THEN FALSE
    ELSE TRUE
END;
