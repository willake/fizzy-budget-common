CREATE DATABASE  IF NOT EXISTS `fizzybudget_user_db`;

USE `fizzybudget_user_db`;

DROP TABLE IF EXISTS `user_role`;
DROP TABLE IF EXISTS `app_user`;
DROP TABLE IF EXISTS `role`;

CREATE TABLE `app_user` (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    activated tinyint(1) NOT NULL DEFAULT 0,
    provider VARCHAR(50) DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO `app_user` (username, email, password_hash, activated, provider, created_at, updated_at)
VALUES 
('user', 'user@gmail.com', 'user', 1, NULL, NOW(), NOW());

CREATE TABLE `role` (
    role_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL
);

INSERT INTO `role` (role_name)
VALUES 
('ROLE_USER'),
('ROLE_MANAGER'),
('ROLE_ADMIN');

CREATE TABLE `user_role` (
    user_id BIGINT,
    role_id BIGINT,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES `app_user`(user_id),
    FOREIGN KEY (role_id) REFERENCES `role`(role_id),
    PRIMARY KEY (user_id, role_id)
);

INSERT INTO `user_role` (user_id, role_id, created_at, updated_at)
VALUES 
(1, 1, NOW(), NOW()),
(1, 2, NOW(), NOW()),
(1, 3, NOW(), NOW());

DROP TABLE IF EXISTS `recurrent_expense`;
DROP TABLE IF EXISTS `expense`;
DROP TABLE IF EXISTS `currency`;
DROP TABLE IF EXISTS `category`;

CREATE TABLE `currency` (
    currency_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    currency_code VARCHAR(10) NOT NULL UNIQUE, -- e.g., USD, EUR
    currency_name VARCHAR(50) NOT NULL,       -- Full name, e.g., US Dollar, Euro
    currency_symbol VARCHAR(5) NOT NULL DEFAULT '$'    -- e.g., $, €
);

CREATE TABLE `category` (
    category_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL UNIQUE,
    category_description TEXT
);

CREATE TABLE `expense` (
    expense_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    currency_id BIGINT NOT NULL,
    expense_amount DECIMAL(15, 2) NOT NULL,           -- Expense amount
    expense_description TEXT,
    expense_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES `app_user`(user_id),
    FOREIGN KEY (category_id) REFERENCES `category`(category_id),
    FOREIGN KEY (currency_id) REFERENCES `currency`(currency_id)
);

CREATE TABLE `recurrent_expense` (
    expense_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    currency_id BIGINT NOT NULL,
    expense_amount DECIMAL(15, 2) NOT NULL,           -- Recurrent amount
    expense_description TEXT,
    expense_frequency ENUM('DAILY', 'WEEKLY', 'MONTHLY', 'YEARLY') NOT NULL,  -- Frequency of recurrence
    start_date DATE NOT NULL,
    end_date DATE,                             -- NULL means indefinite
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES `app_user`(user_id),
    FOREIGN KEY (category_id) REFERENCES `category`(category_id),
    FOREIGN KEY (currency_id) REFERENCES `currency`(currency_id)
);