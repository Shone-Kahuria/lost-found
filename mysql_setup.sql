-- Create database
CREATE DATABASE IF NOT EXISTS lostandfounddb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Use the database
USE lostandfounddb;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create lost_found_items table
CREATE TABLE IF NOT EXISTS lost_found_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    description VARCHAR(1000),
    category VARCHAR(255),
    location VARCHAR(255),
    dateLostFound DATE,
    contactInfo VARCHAR(255),
    status VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create MySQL user for the application
-- Replace 'appuser' and 'apppassword' with your preferred username and password
CREATE USER IF NOT EXISTS 'appuser'@'localhost' IDENTIFIED WITH mysql_native_password BY 'apppassword';

-- Grant privileges to the user on the database
GRANT ALL PRIVILEGES ON lostandfounddb.* TO 'appuser'@'localhost';

-- Flush privileges to apply changes
FLUSH PRIVILEGES;
