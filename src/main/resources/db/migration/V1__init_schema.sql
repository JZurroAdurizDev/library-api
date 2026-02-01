-- V1__init_schema.sql
-- Schema for library-api (MySQL 8+)

-- IMPORTANT:
-- This migration should NOT create the database or users.
-- It assumes the schema/database already exists and is selected.

-- -------------------------
-- ROLES
-- -------------------------
CREATE TABLE IF NOT EXISTS roles (
    id        INT NOT NULL AUTO_INCREMENT,
    role_name VARCHAR(50) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_roles_role_name (role_name)
) ENGINE=InnoDB;

-- -------------------------
-- USERS
-- -------------------------
CREATE TABLE IF NOT EXISTS users (
    id            INT NOT NULL AUTO_INCREMENT,
    dni           VARCHAR(9)   NOT NULL,
    first_name    VARCHAR(100) NOT NULL,
    last_name     VARCHAR(100) NOT NULL,
    email         VARCHAR(150) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role_id       INT NOT NULL,

    PRIMARY KEY (id),
    UNIQUE KEY uq_users_dni (dni),
    UNIQUE KEY uq_users_email (email),
    KEY ix_users_role_id (role_id),

    CONSTRAINT fk_users_role
        FOREIGN KEY (role_id) REFERENCES roles(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- -------------------------
-- BOOKS
-- -------------------------
CREATE TABLE IF NOT EXISTS books (
    id             INT NOT NULL AUTO_INCREMENT,
    title          VARCHAR(200) NOT NULL,
    author         VARCHAR(150) NOT NULL,
    isbn           VARCHAR(13)  NOT NULL,
    published_year YEAR         NOT NULL,
    pages          INT          NULL,

    PRIMARY KEY (id),
    UNIQUE KEY uq_books_isbn (isbn)
) ENGINE=InnoDB;

-- -------------------------
-- GENRES
-- -------------------------
CREATE TABLE IF NOT EXISTS genres (
    id         INT NOT NULL AUTO_INCREMENT,
    genre_name VARCHAR(50) NOT NULL,

    PRIMARY KEY (id),
    UNIQUE KEY uq_genres_genre_name (genre_name)
) ENGINE=InnoDB;

-- -------------------------
-- BOOK_GENRES (N:M)
-- -------------------------
CREATE TABLE IF NOT EXISTS book_genres (
    book_id  INT NOT NULL,
    genre_id INT NOT NULL,

    PRIMARY KEY (book_id, genre_id),
    KEY ix_book_genres_genre_id (genre_id),

    CONSTRAINT fk_book_genres_book
        FOREIGN KEY (book_id) REFERENCES books(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT fk_book_genres_genre
        FOREIGN KEY (genre_id) REFERENCES genres(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- -------------------------
-- LOANS (cabecera)
-- -------------------------
CREATE TABLE IF NOT EXISTS loans (
    id         INT NOT NULL AUTO_INCREMENT,
    user_id    INT NOT NULL,
    start_date DATE NOT NULL,
    due_date   DATE NOT NULL,
    closed_at  DATETIME NULL,
    status     VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

    PRIMARY KEY (id),
    KEY ix_loans_user_id (user_id),
    KEY ix_loans_status (status),
    KEY ix_loans_due_date (due_date),

    CONSTRAINT ck_loans_status
        CHECK (status IN ('ACTIVE','CLOSED')),

    CONSTRAINT ck_loans_dates
        CHECK (start_date <= due_date),

    CONSTRAINT fk_loans_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- -------------------------
-- LOAN_ITEMS (detalle)
-- -------------------------
CREATE TABLE IF NOT EXISTS loan_items (
    loan_id INT NOT NULL,
    book_id INT NOT NULL,

    PRIMARY KEY (loan_id, book_id),
    KEY ix_loan_items_book_id (book_id),

    CONSTRAINT fk_loan_items_loan
        FOREIGN KEY (loan_id) REFERENCES loans(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT fk_loan_items_book
        FOREIGN KEY (book_id) REFERENCES books(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB;
