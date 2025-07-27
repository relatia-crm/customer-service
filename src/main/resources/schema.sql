-- =============================================
-- Customer Table Schema (MySQL & H2 compatible)
-- =============================================

-- Create customer table if it doesn't exist
CREATE TABLE IF NOT EXISTS customers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    address VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    is_deleted BOOLEAN DEFAULT FALSE,
    version BIGINT DEFAULT 0,
    CONSTRAINT uk_customer_email UNIQUE (email)
);
