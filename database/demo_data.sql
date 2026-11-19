-- =====================================================
-- News Agent Delivery System - DEMO DATA
-- Run AFTER newsagent_db.sql, on an EMPTY database.
-- Gives every menu something to show and lets the DAO tests run
-- (they expect customer 1, delivery area 1 and delivery person 1 to exist).
-- To start again: DROP DATABASE newsagent_db; then run both scripts again.
-- =====================================================
USE newsagent_db;

-- Delivery people and areas (Ahmed - US 12, 17, 32)
INSERT INTO DeliveryPersonTable (firstName, surname, phoneNumber, status) VALUES
 ('Tom',  'Ryan',   '0871111111', 'ACTIVE'),
 ('Anna', 'Walsh',  '0872222222', 'ACTIVE'),
 ('Liam', 'Byrne',  '0873333333', 'INACTIVE');

INSERT INTO DeliveryAreaTable (areaName, deliveryPersonId) VALUES
 ('Athlone Town', 1),
 ('Monksland',    2),
 ('Coosan',       1);

-- Customers (Rodion - US 1, 7, 26)
INSERT INTO CustomerTable (firstName, surname, address, phoneNumber, deliveryAreaId, status) VALUES
 ('John',  'Murphy',   '12 Church Street, Athlone', '0861234567', 1, 'ACTIVE'),
 ('Mary',  'Kelly',    '4 Dublin Road, Athlone',    '0861234568', 1, 'ACTIVE'),
 ('Sean',  'O''Brien', '7 Monksland Ave',           '0861234569', 2, 'ACTIVE'),
 ('Aoife', 'Doyle',    '22 Coosan Point',           '0861234570', 3, 'ACTIVE'),
 ('Peter', 'Quinn',    '3 Old Mill, Athlone',       '0861234571', 1, 'CLOSED');

-- Publications and price history (Dylan - US 8, 9, 27, 29, 31)
INSERT INTO publication (publicationName, publicationType, publicationPrice, stockQuantity,
                         publicationFrequency, releaseDay, lowStockThreshold) VALUES
 ('Irish Times',       'NEWSPAPER', 2.00, 40, 'DAILY',  'DAILY',  5),
 ('Irish Independent', 'NEWSPAPER', 1.50, 30, 'DAILY',  'DAILY',  5),
 ('Westmeath Independent', 'NEWSPAPER', 2.50, 3, 'WEEKLY', 'WEDNESDAY', 5),
 ('RTE Guide',         'MAGAZINE',  3.00, 10, 'WEEKLY', 'TUESDAY', 2);

INSERT INTO publication_price_history (publicationId, price, startDate) VALUES
 (1, 1.80, '2026-10-01'),
 (1, 2.00, '2026-10-15'),
 (2, 1.50, '2026-10-01');

-- Orders and a holiday (Patrick - US 2, 3, 5)
INSERT INTO OrderTable (customerId, publicationId, deliveryDays, orderStartDate, orderEndDate) VALUES
 (1, 1, 'DAILY',                    '2026-10-01', NULL),
 (1, 4, 'TUESDAY',                  '2026-10-01', NULL),
 (2, 2, 'MONDAY,WEDNESDAY,FRIDAY',  '2026-10-01', NULL),
 (3, 1, 'DAILY',                    '2026-10-01', NULL),
 (4, 3, 'WEDNESDAY',                '2026-10-01', NULL);

INSERT INTO HolidayTable (customerId, holidayStartDate, holidayEndDate) VALUES
 (2, '2026-12-20', '2026-12-28');

-- Recorded deliveries for October 2026 (US 14, 15, 24) - used by billing and reports
INSERT INTO DeliveryTable (orderId, customerId, publicationId, deliveryDate, deliveryStatus, missedReason) VALUES
 (1, 1, 1, '2026-10-05', 'DELIVERED',    NULL),
 (1, 1, 1, '2026-10-12', 'DELIVERED',    NULL),
 (1, 1, 1, '2026-10-19', 'DELIVERED',    NULL),
 (1, 1, 1, '2026-10-26', 'MISSED',       'Gate locked'),
 (2, 1, 4, '2026-10-06', 'DELIVERED',    NULL),
 (2, 1, 4, '2026-10-13', 'DELIVERED',    NULL),
 (3, 2, 2, '2026-10-05', 'DELIVERED',    NULL),
 (3, 2, 2, '2026-10-07', 'DELIVERED',    NULL),
 (3, 2, 2, '2026-10-09', 'OUT OF STOCK', NULL),
 (4, 3, 1, '2026-10-20', 'DELIVERED',    NULL),
 (4, 3, 1, '2026-10-21', 'DELIVERED',    NULL),
 (5, 4, 3, '2026-10-14', 'DELIVERED',    NULL);
