-- =====================================================
-- News Agent Delivery System - database structure
-- Run this file in MySQL Workbench (lightning icon).
-- CREATE TABLE IF NOT EXISTS preserves existing tables and data.
-- Note: IF NOT EXISTS does not update an existing table's structure.
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
-- Uses 'publication' to match the current PublicationDAO.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS publication (
    publicationId INT AUTO_INCREMENT PRIMARY KEY,
    publicationName VARCHAR(60) NOT NULL UNIQUE,
    publicationType VARCHAR(20) NOT NULL,
    publicationPrice DECIMAL(5,2) NOT NULL,
    stockQuantity INT NOT NULL DEFAULT 0,
    publicationFrequency VARCHAR(10) NOT NULL DEFAULT 'DAILY',
    releaseDay VARCHAR(15) NOT NULL DEFAULT 'DAILY',
    lowStockThreshold INT NOT NULL DEFAULT 5
);

-- Publication price history used by PublicationDAO
CREATE TABLE IF NOT EXISTS publication_price_history (
    priceHistoryId INT AUTO_INCREMENT PRIMARY KEY,
    publicationId INT NOT NULL,
    price DECIMAL(5,2) NOT NULL,
    startDate DATE NOT NULL,
    FOREIGN KEY (publicationId)
        REFERENCES publication(publicationId) ON DELETE CASCADE
);

-- -----------------------------------------------------
-- Delivery Area + Delivery Person (Ahmed) - User Stories 17, 32, 12
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS DeliveryPersonTable (
    deliveryPersonId INT AUTO_INCREMENT PRIMARY KEY,
    firstName VARCHAR(50) NOT NULL,
    surname VARCHAR(50) NOT NULL,
    phoneNumber VARCHAR(10) NOT NULL,
    status VARCHAR(10) NOT NULL DEFAULT 'ACTIVE'
);

CREATE TABLE IF NOT EXISTS DeliveryAreaTable (
    deliveryAreaId INT AUTO_INCREMENT PRIMARY KEY,
    areaName VARCHAR(30) NOT NULL UNIQUE,
    deliveryPersonId INT,
    FOREIGN KEY (deliveryPersonId)
        REFERENCES DeliveryPersonTable(deliveryPersonId)
);

-- -----------------------------------------------------
-- Daily Delivery Docket (Ahmed) - User Story 11
-- -----------------------------------------------------
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
-- Orders + Holidays (Patrick) - User Stories 2, 3, 4, 5, 6
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS OrderTable (
    orderId INT AUTO_INCREMENT PRIMARY KEY,
    customerId INT NOT NULL,
    publicationId INT NOT NULL,
    deliveryDays VARCHAR(100) NOT NULL,
    orderStartDate DATE NOT NULL,
    orderEndDate DATE,
    FOREIGN KEY (customerId)
        REFERENCES CustomerTable(customerId),
    FOREIGN KEY (publicationId)
        REFERENCES publication(publicationId)
);

CREATE TABLE IF NOT EXISTS HolidayTable (
    holidayId INT AUTO_INCREMENT PRIMARY KEY,
    customerId INT NOT NULL,
    holidayStartDate DATE NOT NULL,
    holidayEndDate DATE NOT NULL,
    FOREIGN KEY (customerId)
        REFERENCES CustomerTable(customerId)
);

-- Inspect created/existing tables.
-- REVIEW FIRST in MySQL Workbench. This adds tables ONLY; it does not migrate existing incompatible tables.
-- The team's currently supplied PublicationDAO.java uses `publication`, NOT `PublicationTable`.
-- Run only after inspecting SHOW TABLES and SHOW CREATE TABLE statements.
USE newsagent_db;

CREATE TABLE IF NOT EXISTS DeliveryTable (
    deliveryId INT AUTO_INCREMENT PRIMARY KEY,
    orderId INT NOT NULL,
    customerId INT NOT NULL,
    publicationId INT NOT NULL,
    deliveryDate DATE NOT NULL,
    deliveryStatus VARCHAR(20) NOT NULL,
    missedReason VARCHAR(100) NULL,
    CONSTRAINT chk_delivery_record_status CHECK (deliveryStatus IN ('DELIVERED','MISSED','OUT OF STOCK')),
    CONSTRAINT fk_delivery_record_order FOREIGN KEY (orderId) REFERENCES OrderTable(orderId),
    CONSTRAINT fk_delivery_record_customer FOREIGN KEY (customerId) REFERENCES CustomerTable(customerId),
    CONSTRAINT fk_delivery_record_publication FOREIGN KEY (publicationId) REFERENCES publication(publicationId)
);

CREATE TABLE IF NOT EXISTS DeliveryDocketLineTable (
    docketLineId INT AUTO_INCREMENT PRIMARY KEY,
    docketId INT NOT NULL,
    orderId INT NOT NULL,
    customerId INT NOT NULL,
    publicationId INT NOT NULL,
    deliveryDate DATE NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    deliveryStatus VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    missedReason VARCHAR(100) NULL,
    CONSTRAINT uq_docket_line_order UNIQUE (docketId, orderId),
    CONSTRAINT fk_line_docket FOREIGN KEY (docketId) REFERENCES DeliveryDocketTable(docketId),
    CONSTRAINT fk_line_order FOREIGN KEY (orderId) REFERENCES OrderTable(orderId),
    CONSTRAINT fk_line_customer FOREIGN KEY (customerId) REFERENCES CustomerTable(customerId),
    CONSTRAINT fk_line_publication FOREIGN KEY (publicationId) REFERENCES publication(publicationId),
    CONSTRAINT chk_docket_line_status CHECK (deliveryStatus IN ('PENDING','DELIVERED','MISSED','OUT OF STOCK','RETURNED')),
    CONSTRAINT chk_docket_line_quantity CHECK (quantity > 0)
);

CREATE TABLE IF NOT EXISTS StockHistoryTable (
    stockHistoryId INT AUTO_INCREMENT PRIMARY KEY,
    publicationId INT NOT NULL,
    movementType VARCHAR(20) NOT NULL,
    quantityChanged INT NOT NULL,
    previousStock INT NOT NULL,
    newStock INT NOT NULL,
    movementDate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    reason VARCHAR(255),
    docketId INT NULL,
    CONSTRAINT fk_stock_history_publication FOREIGN KEY (publicationId) REFERENCES publication(publicationId),
    CONSTRAINT fk_stock_history_docket FOREIGN KEY (docketId) REFERENCES DeliveryDocketTable(docketId),
    CONSTRAINT chk_stock_history_quantity CHECK (quantityChanged > 0)
);
SHOW TABLES;

-- NOTE: Patrick's DeliveryTable, docket-line and stock-history tables
-- are not included yet; verify their DAO schemas before adding them.
-- -----------------------------------------------------
-- Billing (Rodion) - User Stories 19, 20, 21, 22, 28
-- One bill per customer per month (UNIQUE stops a second bill for the same month)
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS BillTable (
    billId       INT AUTO_INCREMENT PRIMARY KEY,
    customerId   INT          NOT NULL,
    billingYear  INT          NOT NULL,
    billingMonth INT          NOT NULL,
    totalAmount  DECIMAL(8,2) NOT NULL DEFAULT 0.00,
    billStatus   VARCHAR(10)  NOT NULL DEFAULT 'UNPAID',
    paymentDate  DATE         NULL,
    CONSTRAINT uq_bill_customer_month UNIQUE (customerId, billingYear, billingMonth),
    CONSTRAINT fk_bill_customer FOREIGN KEY (customerId) REFERENCES CustomerTable(customerId)
);
