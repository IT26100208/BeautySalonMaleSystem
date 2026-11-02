CREATE TABLE IF NOT EXISTS system_settings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    salon_name VARCHAR(255),
    opening_time VARCHAR(50),
    closing_time VARCHAR(50),
    contact_phone VARCHAR(50),
    contact_email VARCHAR(255),
    currency_symbol VARCHAR(10),
    max_appointments_per_slot INT
);