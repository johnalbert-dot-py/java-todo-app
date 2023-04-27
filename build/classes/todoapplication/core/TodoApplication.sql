-- Active: 1681917387186@@127.0.0.1@3306

CREATE DATABASE `TodoApplicationDB`;

USE `TodoApplicationDB`;

CREATE TABLE
    IF NOT EXISTS `User` (
        `id` int NOT NULL AUTO_INCREMENT,
        `username` varchar(255) NOT NULL UNIQUE,
        `password` varchar(255) NOT NULL,
        `created_at` datetime NOT NULL,
        `updated_at` datetime NOT NULL,
        PRIMARY KEY (`id`)
    );

CREATE TABLE
    IF NOT EXISTS `Todo` (
        `id` int NOT NULL AUTO_INCREMENT,
        `title` varchar(255) NOT NULL,
        `description` TEXT NOT NULL,
        `due_date` DATE NOT NULL,
        `is_completed` BOOLEAN NOT NULL,
        `created_at` datetime NOT NULL,
        `updated_at` datetime NOT NULL,
        `user_id` int NOT NULL,
        PRIMARY KEY (`id`),
        FOREIGN KEY (`user_id`) REFERENCES `User`(`id`)
    );

-- set the created_at to auto now add

ALTER TABLE
    `Todo` CHANGE `created_at` `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE
    `Todo` CHANGE `updated_at` `updated_at` DATETIME NOT NULL ON UPDATE CURRENT_TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE
    `User` CHANGE `created_at` `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE
    `User` CHANGE `updated_at` `updated_at` DATETIME NOT NULL ON UPDATE CURRENT_TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

;