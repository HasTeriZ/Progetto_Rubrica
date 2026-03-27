-- Crea il database (se non esiste già)
CREATE DATABASE IF NOT EXISTS rubrica
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE rubrica;

-- --------------------------------------------------------
-- Tabella: persone
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS persone (
    id        INT          NOT NULL AUTO_INCREMENT,
    nome      VARCHAR(100) NOT NULL,
    cognome   VARCHAR(100) NOT NULL,
    indirizzo VARCHAR(255)          DEFAULT '',
    telefono  VARCHAR(50)           DEFAULT '',
    eta       INT          NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------
-- Tabella: utenti
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS utenti (
    id       INT          NOT NULL AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Inserisce l'utente di default: admin / admin
INSERT IGNORE INTO utenti (username, password) VALUES ('admin', 'admin');

