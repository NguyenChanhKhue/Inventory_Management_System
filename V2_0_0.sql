-- Inventory Management System - Full Database Script
-- Version: 2.0.0
-- Created: 2026-04-09

-- ========================
-- RESET DATABASE
-- ========================
DROP DATABASE IF EXISTS inventory_DB;
CREATE DATABASE inventory_DB;
USE inventory_DB;

-- ========================
-- 1. USERS
-- ========================
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    role VARCHAR(50),
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_users_email (email)
);

-- ========================
-- 2. CATEGORIES
-- ========================
CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    INDEX idx_categories_name (name)
);

-- ========================
-- 3. SUPPLIERS
-- ========================
CREATE TABLE suppliers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    contact_info VARCHAR(255) NOT NULL,
    address VARCHAR(500),
    INDEX idx_suppliers_name (name)
);

-- ========================
-- 4. PRODUCTS
-- ========================
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    sku VARCHAR(100) NOT NULL UNIQUE,
    price DECIMAL(10, 2) NOT NULL,
    stock_quantity INT NOT NULL DEFAULT 0,
    description TEXT,
    exp_date_time TIMESTAMP NULL,
    img_url VARCHAR(500),
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    category_id BIGINT,
    CONSTRAINT fk_products_category FOREIGN KEY (category_id)
        REFERENCES categories(id) ON DELETE SET NULL,
    INDEX idx_products_sku (sku),
    INDEX idx_products_category_id (category_id),
    INDEX idx_products_name (name),
    INDEX idx_products_stock_quantity (stock_quantity)
);

-- ========================
-- 5. TRANSACTIONS
-- ========================
CREATE TABLE transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    total_product INT NOT NULL,
    total_price DECIMAL(10, 2),
    transaction_type VARCHAR(50),
    transaction_status VARCHAR(50),
    description TEXT,
    note TEXT,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    user_id BIGINT,
    product_id BIGINT,
    supplier_id BIGINT,
    CONSTRAINT fk_transactions_user FOREIGN KEY (user_id)
        REFERENCES users(id) ON DELETE SET NULL,
    CONSTRAINT fk_transactions_product FOREIGN KEY (product_id)
        REFERENCES products(id) ON DELETE SET NULL,
    CONSTRAINT fk_transactions_supplier FOREIGN KEY (supplier_id)
        REFERENCES suppliers(id) ON DELETE SET NULL,
    INDEX idx_transactions_user_id (user_id),
    INDEX idx_transactions_product_id (product_id),
    INDEX idx_transactions_supplier_id (supplier_id),
    INDEX idx_transactions_type (transaction_type),
    INDEX idx_transactions_status (transaction_status),
    INDEX idx_transactions_create_at (create_at)
);

-- ========================
-- 6. PASSWORD RESET TOKENS
-- ========================
CREATE TABLE password_reset_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    token_hash VARCHAR(64) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    otp_verified BOOLEAN NOT NULL DEFAULT FALSE,
    used BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_password_reset_tokens_user FOREIGN KEY (user_id)
        REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_password_reset_tokens_user_id (user_id),
    INDEX idx_password_reset_tokens_expires_at (expires_at),
    INDEX idx_password_reset_tokens_otp_verified (otp_verified),
    INDEX idx_password_reset_tokens_used (used)
);
