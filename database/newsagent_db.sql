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
-- CREATE TABLE IF NOT EXISTS PublicationTable ( ... );

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
-- -----------------------------------------------------
-- Delivery Area + Delivery Person (Ahmed) - User Stories 17,12
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS DeliveryAreaTable (
    deliveryAreaId INT AUTO_INCREMENT PRIMARY KEY,
    areaName VARCHAR(30) NOT NULL UNIQUE,
    deliveryPersonId INT,

    FOREIGN KEY (deliveryPersonId)
        REFERENCES DeliveryPersonTable(deliveryPersonId)
);
-- -----------------------------------------------------
-- Orders + Holidays (Patrick) - User Stories 2, 3, 4, 5, 6
-- -----------------------------------------------------
-- CREATE TABLE IF NOT EXISTS OrderTable ( ... );
-- CREATE TABLE IF NOT EXISTS HolidayTable ( ... );
