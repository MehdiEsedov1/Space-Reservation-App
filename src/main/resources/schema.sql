-- schema.sql
-- schema for 'space_reservation_app'
-- Creates tables: workspaces, reservations, users
-- Author: Mehdi
-- Date: 2025-07-04

-- Drop existing tables if they exist (for re-runnable scripts)
DROP TABLE IF EXISTS reservations;
DROP TABLE IF EXISTS workspaces;
DROP TABLE IF EXISTS users;

-- Table: workspaces
CREATE TABLE workspaces
(
    id    SERIAL PRIMARY KEY,
    name  VARCHAR(100)   NOT NULL,
    type  VARCHAR(50)    NOT NULL,
    price NUMERIC(10, 2) NOT NULL CHECK (price >= 0)
);

-- Table: reservations
CREATE TABLE reservations
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    space_id   INT          NOT NULL,
    start_time TIMESTAMP    NOT NULL,
    end_time   TIMESTAMP    NOT NULL,
    CONSTRAINT fk_space FOREIGN KEY (space_id) REFERENCES workspaces (id),
    CONSTRAINT chk_time CHECK (start_time < end_time)
);

-- Table: users
CREATE TABLE users
(
    username VARCHAR(100) PRIMARY KEY,
    role     VARCHAR(50) NOT NULL
);
