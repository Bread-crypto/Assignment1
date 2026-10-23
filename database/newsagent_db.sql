-- =====================================================
-- News Agent Delivery System - database structure
-- Run this whole file in MySQL Workbench (lightning icon).
-- Safe to run more than once (IF NOT EXISTS).
-- Each team member adds their own table below.
-- =====================================================

CREATE DATABASE IF NOT EXISTS newsagent_db;
USE newsagent_db;

-- -----------------------------------------------------
-- Customer (Rodion) - User Stories 1, 7, 26
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS CustomerTable (
  customerId     INT AUTO_INCREMENT PRIMARY KEY,
  firstName      VARCHAR(50)  NOT NULL,
  surname        VARCHAR(50)  NOT NULL,
  address        VARCHAR(100) NOT NULL,
  phoneNumber    VARCHAR(10)  NOT NULL,
  deliveryAreaId INT          NOT NULL,
  status         VARCHAR(10)  NOT NULL DEFAULT 'ACTIVE'
);

-- -----------------------------------------------------
-- Publication (Dylan) - User Stories 27, 29, 31, 8, 9, 10
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS PublicationTable (
    publicationId INT AUTO_INCREMENT PRIMARY KEY,
    publicationName VARCHAR(60) NOT NULL UNIQUE,
    publicationType VARCHAR(20) NOT NULL,
    publicationPrice DECIMAL(6,2) NOT NULL,
    stockQuantity INT NOT NULL DEFAULT 0,
    lowStockThreshold INT NOT NULL DEFAULT 5,
    publicationFrequency VARCHAR(20) NOT NULL DEFAULT 'DAILY',
    releaseDay VARCHAR(20) NOT NULL DEFAULT 'DAILY',

    CONSTRAINT chk_publication_price
        CHECK (publicationPrice BETWEEN 0.01 AND 100.00),

    CONSTRAINT chk_publication_stock
        CHECK (stockQuantity >= 0),

    CONSTRAINT chk_publication_threshold
        CHECK (lowStockThreshold >= 0),

    CONSTRAINT chk_publication_type
        CHECK (publicationType IN ('NEWSPAPER', 'MAGAZINE')),

    CONSTRAINT chk_publication_frequency
        CHECK (publicationFrequency IN ('DAILY', 'WEEKLY', 'MONTHLY'))
);
CREATE TABLE IF NOT EXISTS PublicationTable (
    publicationId INT AUTO_INCREMENT PRIMARY KEY,
    publicationName VARCHAR(60) NOT NULL UNIQUE,
    publicationType VARCHAR(20) NOT NULL,
    publicationPrice DECIMAL(6,2) NOT NULL,
    stockQuantity INT NOT NULL DEFAULT 0,
    lowStockThreshold INT NOT NULL DEFAULT 5,
    publicationFrequency VARCHAR(20) NOT NULL DEFAULT 'DAILY',
    releaseDay VARCHAR(20) NOT NULL DEFAULT 'DAILY'
);

CREATE TABLE IF NOT EXISTS PublicationPriceHistoryTable (
    priceHistoryId INT AUTO_INCREMENT PRIMARY KEY,
    publicationId INT NOT NULL,
    price DECIMAL(6,2) NOT NULL,
    startDate DATE NOT NULL,
    FOREIGN KEY (publicationId)
        REFERENCES PublicationTable(publicationId)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS StockHistoryTable (
    stockHistoryId INT AUTO_INCREMENT PRIMARY KEY,
    publicationId INT NOT NULL,
    movementType VARCHAR(20) NOT NULL,
    quantity INT NOT NULL,
    previousStock INT NOT NULL,
    newStock INT NOT NULL,
    movementDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (publicationId)
        REFERENCES PublicationTable(publicationId),
    CHECK (quantity > 0),
    CHECK (movementType IN ('RECEIVED', 'ISSUED', 'RETURNED'))
);

-- ---------------------------------------------
-- Daily Delivery Docket (Ahmed) - User Story 11
-- ---------------------------------------------

CREATE TABLE IF NOT EXISTS DeliveryDocketTable (
    docketId INT AUTO_INCREMENT PRIMARY KEY,
    docketDate DATE NOT NULL,
    deliveryAreaId INT NOT NULL,
    deliveryPersonId INT NOT NULL,

    FOREIGN KEY (deliveryAreaId)
        REFERENCES DeliveryAreaTable(deliveryAreaId),

    FOREIGN KEY (deliveryPersonId)
        REFERENCES DeliveryPersonTable(deliveryPersonId)
);
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Orders + Holidays (Patrick)
-- User Stories 2, 3, 4, 5, 6
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS OrderTable (
    orderId INT AUTO_INCREMENT PRIMARY KEY,
    customerId INT NOT NULL,
    publicationId INT NOT NULL,
    deliveryDays VARCHAR(100) NOT NULL,
    orderStartDate DATE NOT NULL,
    orderEndDate DATE NULL,

    CONSTRAINT fk_order_customer
        FOREIGN KEY (customerId)
        REFERENCES CustomerTable(customerId),

    CONSTRAINT fk_order_publication
        FOREIGN KEY (publicationId)
        REFERENCES PublicationTable(publicationId)
);

CREATE TABLE IF NOT EXISTS HolidayTable (
    holidayId INT AUTO_INCREMENT PRIMARY KEY,
    customerId INT NOT NULL,
    holidayStartDate DATE NOT NULL,
    holidayEndDate DATE NOT NULL,

    CONSTRAINT fk_holiday_customer
        FOREIGN KEY (customerId)
        REFERENCES CustomerTable(customerId),

    CONSTRAINT chk_holiday_dates
        CHECK (holidayEndDate >= holidayStartDate)
);

