
# News Agent Delivery System

**Software Engineering 3 — Team Assignment | TUS Athlone | 2026**

A Java console application for managing a local newsagent's customers, publications, subscriptions, delivery areas, daily delivery dockets and monthly billing.

The project uses Java, Eclipse, JUnit 5, MySQL and GitHub, following an Agile Scrum development approach with Test-Driven Development (TDD).

> **Project Status — 19 November 2026:** The integrated application is available in `master` following Pull Request #8. Customer Management, Delivery Areas and People, Billing, Delivery Dockets and Daily Reporting are implemented. Publication and Order Management from Wave 1 are also integrated. Patrick's delivery recording and Dylan's additional stock/history functionality from Wave 2 are pending final integration. Demonstration delivery records are provided through SQL test data.

## 1. Project Overview

The News Agent Delivery System is designed to support the daily operations of a local newspaper and magazine delivery business.

The application allows a newsagent to:

- Manage customer accounts and delivery areas.
- Manage newspapers, magazines and stock.
- Create and maintain customer subscriptions.
- Record customer holidays.
- Generate daily delivery dockets.
- View delivery status and daily reports.
- Generate monthly customer bills.
- Record payments and monitor outstanding balances.

The application is implemented as a Java console system with a shared MySQL database.

## 2. Team Responsibilities

| Team Member | Wave 1 | Wave 2 | Additional Responsibility |
|---|---|---|---|
| Rodion Omelich | Customer Management (US 1, 7, 26) | Billing (US 19, 20, 21, 22, 28) | MainMenu and integration |
| Ahmed Sawan | Delivery Areas and People (US 12, 17, 32) | Dockets and Reporting (US 11, 16, 18, 30) | Delivery integration |
| Patrick Byrne | Orders and Holidays (US 2, 3, 4, 5, 6) | Recording Deliveries (US 14, 15) | Delivery records |
| Dylan Kirwan | Publications and Stock (US 8, 9, 10, 27, 29, 31) | Stock on Dockets and Delivery History (US 13, 23, 24) | Stock management |

### Current Implementation Status

| Component | Status |
|---|---|
| Customer Management | Implemented |
| Billing and Payments | Implemented |
| MainMenu | Implemented |
| Delivery Areas and People | Implemented |
| Delivery Dockets and Reporting | Implemented |
| Publications — Wave 1 | Integrated |
| Orders and Holidays — Wave 1 | Integrated |
| Delivery Recording — Patrick Wave 2 | Pending final integration |
| Stock and Delivery History — Dylan Wave 2 | Pending final integration |

The current version demonstrates the main business workflow using predefined delivery records. Completion of an individual module does not automatically mean that every cross-module acceptance criterion has been verified.

## 3. Application Architecture

The system follows a layered architecture that separates user interaction, business logic and database operations.

| Layer | Examples | Responsibility |
|---|---|---|
| Presentation | MainMenu, CustomerCLI, BillCLI, DeliveryWave2CLI | User input, menus and output |
| Business / Domain | Customer, Bill, Publication, DeliveryDocketGenerator | Validation, business rules and calculations |
| Persistence | CustomerDAO, BillDAO, OrderDAO, PublicationDAO | Database operations using JDBC |
| Database | MySQL newsagent_db | Data storage and relationships |

### Architecture Overview

```text
                   MainMenu.java
                         |
       ---------------------------------------
       |           |           |             |
   CustomerCLI  OrderCLI  PublicationCLI   BillCLI
       |           |           |             |
   Customer      Order     Publication       Bill
       |           |           |             |
   CustomerDAO  OrderDAO  PublicationDAO   BillDAO
       |           |           |             |
       ---------------------------------------
                         |
                  DatabaseConnection
                         |
                       MySQL
                    newsagent_db
```

Additional delivery management, docket and reporting modules use the same database and architectural approach.

The separation of responsibilities improves maintainability, testability and integration.

## 4. Main Menu

The application starts from:

`Assignment1/src/MainMenu.java`

```text
========== NEWS AGENT DELIVERY SYSTEM ==========
1. Customers
2. Publications & Stock
3. Orders & Holidays
4. Delivery Areas & Delivery People
5. Delivery Dockets & Daily Reports
6. Billing
0. Exit
```

MainMenu provides a single entry point and connects the CLI modules developed by the team.

The login component validates the username and password format before opening the main menu.

The current login checks input format rather than authenticating against stored user accounts.

## 5. Wave 1 — Core Entities

### Customer Management — Rodion

Files:

- Customer.java
- CustomerDAO.java
- CustomerCLI.java
- CustomerTest.java

Features:

- Add new customers.
- Find customers by ID.
- Search by surname.
- List all customers.
- Update customer information.
- Close customer accounts.

Customer validation includes:

- First name: 1–50 characters.
- Surname: 1–50 characters.
- Address: 1–100 characters.
- Phone number: exactly 10 digits, starting with 0.
- Delivery area: ID between 1 and 24.

Customers have ACTIVE or CLOSED status.

Closing an account preserves existing records and historical relationships.

### Publications — Dylan

Features include:

- Add publications.
- Find and list publications.
- Update stock quantity.
- Change publication prices.
- Manage publication frequency and release days.
- Remove publications where database relationships permit.

### Orders and Holidays — Patrick

Features include:

- Create orders for existing publications.
- View customer orders.
- Remove a publication from an active subscription.
- Record customer holiday periods.
- View customer holidays.

Orders connect customers and publications using their IDs.

### Delivery Areas and People — Ahmed

Features include:

- Add and manage delivery people.
- Create and update delivery areas.
- Assign active delivery people to areas.
- View delivery areas and assigned delivery people.

Customers are assigned to delivery areas through `deliveryAreaId`.

## 6. Wave 2 — Business Operations

### Billing — Rodion

Files:

- Bill.java
- BillItem.java
- BillDAO.java
- BillCLI.java
- BillTest.java

Implemented user stories:

- US 19 — Generate monthly customer bills.
- US 20 — Generate itemised bills and text files.
- US 21 — Record payments.
- US 22 — Display unpaid bills and outstanding balances.
- US 28 — Generate bills for all active customers.

The Billing module calculates monthly charges using deliveries recorded with the status `DELIVERED`.

The SQL query excludes `MISSED` and `OUT OF STOCK` deliveries.

Publication prices are selected from the historical price records effective on the delivery date.

The application prevents duplicate bills for the same customer and billing period.

Payments update the bill status from `UNPAID` to `PAID`.

Bills are exported as text files using the format:

`bill_<customerId>_<yyyy-mm>.txt`

### Delivery Dockets and Reporting — Ahmed

Implemented features include:

- Generate daily delivery dockets.
- Filter deliveries by date and area.
- Check customer status.
- Check active orders and delivery schedules.
- Exclude customers on holiday.
- Check publication release schedules.
- Print and save delivery dockets.
- Display daily summaries and delivery statuses.

The docket generator uses customer, order, publication, holiday and delivery-area information.

### Delivery Recording — Patrick

The planned workflow records:

- DELIVERED
- MISSED
- OUT OF STOCK

Missed deliveries may include a reason.

The Wave 2 recording component is awaiting final integration into the main application.

### Stock and Delivery History — Dylan

The remaining Wave 2 integration includes docket-driven stock management, out-of-stock handling and delivery history.

The existing demo data includes delivery status examples, but these predefined records do not replace the complete interactive stock workflow.

## 7. Database Design

The application uses MySQL.

Database scripts:

- `database/newsagent_db.sql`
- `database/demo_data.sql`

Main tables include:

| Table | Purpose |
|---|---|
| CustomerTable | Customer details and account status |
| publication | Publication details, prices and stock |
| publication_price_history | Historical publication prices |
| OrderTable | Customer subscriptions |
| HolidayTable | Customer holiday periods |
| DeliveryAreaTable | Delivery areas |
| DeliveryPersonTable | Delivery people |
| DeliveryDocketTable | Daily docket records |
| DeliveryTable | Recorded delivery results |
| DeliveryDocketLineTable | Individual docket lines |
| StockHistoryTable | Stock movements |
| BillTable | Monthly bills and payments |

Foreign keys connect related records and maintain referential integrity.

For example:

- Orders reference customers and publications.
- Bills reference customers.
- Deliveries reference orders, customers and publications.
- Delivery areas reference delivery people.

The database is accessed using JDBC and DAO classes.

## 8. Development Methodology

### Agile Scrum

The team selected Agile Scrum because the project was divided into multiple modules and developed collaboratively.

Development was organised into sprints with regular progress reviews.

### Sprint Plan

| Sprint | Dates | Objective | Status |
|---|---|---|---|
| Sprint 0 | 14–25 September | SDLC justification, repository and requirements gathering | Completed |
| Sprint 1 | 28 September–2 October | User stories, acceptance criteria and project planning | Completed |
| Sprint 2 | 5–23 October | TDD and Wave 1 development | Completed with carried-over work |
| Sprint 3 | 2–13 November | Complete Wave 1 and develop Wave 2 | Partially completed |
| Sprint 4 | 16–20 November | Integration, testing and group submission | In progress |
| Sprint 5 | 23–27 November | Individual reflection | Upcoming |

### Test-Driven Development

The development approach follows the lecturer's TDD example.

```text
User Story
    |
Acceptance Criteria
    |
Test Design
    |
Class Skeleton
    |
JUnit Tests — RED
    |
Implementation — GREEN
    |
Refactoring
    |
Database / DAO
    |
CLI
    |
User Acceptance Testing
```

### Billing TDD Evidence

The development sequence is visible in GitHub history.

| Commit | Description |
|---|---|
| 59143a4 | Billing Test Design |
| f1232f3 | Bill skeleton and failing tests (RED) |
| 85e08db | Billing implementation (GREEN) |
| 8021a39 | BillTable, BillDAO and BillCLI |

This provides evidence that tests were prepared before implementation.

## 9. Git and Team Integration

The team used GitHub for version control.

Each developer worked in a separate feature branch.

Branches were integrated using Git merges and pull requests.

The integration process included:

1. Fetching the latest changes.
2. Reviewing feature branches.
3. Combining compatible implementations.
4. Resolving merge conflicts.
5. Connecting independent CLI modules through MainMenu.
6. Checking database compatibility.
7. Running tests.
8. Merging verified changes into master.

The integrated application was merged into `master` through Pull Request #8 on 19 November 2026.

Repository:

https://github.com/Bread-crypto/Assignment1

## 10. Running the Application

### Requirements

- Eclipse IDE for Java Developers.
- Compatible JDK (the committed Eclipse settings currently specify Java 23).
- MySQL Server.
- MySQL Workbench.
- JUnit 5.
- MySQL Connector/J.

### Installation

1. Clone the repository:

   `https://github.com/Bread-crypto/Assignment1.git`

2. Import the Java project from the inner `Assignment1` directory into Eclipse.

3. Configure the project JDK and add the MySQL JDBC driver to the Build Path.

4. Add JUnit 5 to the Build Path for automated tests.

5. Open MySQL Workbench.

6. Execute:

   `database/newsagent_db.sql`

7. For a fresh demonstration database, execute:

   `database/demo_data.sql`

8. Check the local MySQL connection settings in:

   `Assignment1/src/DatabaseConnection.java`

9. Run:

   `MainMenu.java → Run As → Java Application`

### Login Validation

- Username: 6–12 characters.
- Password: 4–6 digits.
- Maximum attempts: 3.

These are format-validation rules, not production authentication.

### Demonstration Data

The demo database contains:

- Customers and delivery areas.
- Delivery people.
- Publications and price history.
- Customer subscriptions.
- Holiday records.
- Successful deliveries.
- Missed deliveries.
- Out-of-stock delivery examples.

For Billing demonstrations, use October 2026.

Example:

```text
Customer ID: 1
Billing Year: 2026
Billing Month: 10
```

With the original demo records, John Murphy's expected monthly bill is €11.60.

If a bill already exists for that month, the system prevents duplicate generation.

## 11. Testing and Quality Assurance

The project uses JUnit 5 for automated testing.

Test classes include:

- CustomerTest
- BillTest
- LoginTest
- DeliveryAreaTest
- DeliveryPersonTest
- DeliveryDocketGeneratorTest
- DeliveryWave2ServiceTest
- OrderDAOTest
- PublicationTest

Test documentation currently includes:

- `tests/Bill Test Design.docx`
- `tests/UAT.txt`

JUnit tests verify validation, business rules and expected method behaviour.

User Acceptance Testing checks application functionality through the CLI.

A previous local integration check reported 213 out of 214 tests passing, with a reported failure in PublicationTest TC19.

This is a previous result and must be verified after the final integration.

## 12. End-to-End Workflow

The intended complete workflow is:

```text
Create Customer
       |
       v
Create Subscription
       |
       v
Generate Daily Docket
       |
       v
Allocate Stock
       |
       v
Record Delivery Result
       |
       v
Generate Monthly Bill
       |
       v
Record Payment
       |
       v
View Outstanding Balances
```

The current application demonstrates the main workflow using the shared MySQL database and predefined delivery records.

The complete interactive delivery and stock lifecycle requires final integration of the remaining Wave 2 components.

## 13. Known Limitations

### Delivery Recording

Patrick's additional Wave 2 recording functionality is awaiting final integration.

The demonstration database includes predefined delivery records for testing Billing and Reporting.

### Stock and Delivery History

Dylan's full docket-driven stock and delivery-history features are awaiting integration.

### Integration Testing

Cross-module acceptance tests must be rerun following the final merges.

### Regression Testing

A previously reported PublicationTest failure requires verification.

### Database Configuration

The current database connection uses demonstration credentials.

Database credentials should be externalised for a production deployment.

### Login

The login currently validates input format but does not authenticate against stored accounts.

### Database Setup

`CREATE TABLE IF NOT EXISTS` creates missing tables but does not migrate existing database structures.

A fresh database is recommended for repeatable demonstration testing.

## 14. Assignment Submission

### Group Submission

**Deadline: Friday, 20 November 2026 — Week 11**

Worth 40% of the module assessment.

One ZIP submission per group.

Required components:

1. Project Plan
2. Specification
3. Design
4. Source Code
5. Tests
6. System Executable and GitHub Link

### Individual Reflection

**Deadline: Friday, 27 November 2026 — Week 12**

Worth 20% of the module assessment.

Each member submits their own reflection.

## 15. Final Submission Checklist

- [x] Shared GitHub repository established.
- [x] Project plan and user stories created.
- [x] Customer Management implemented.
- [x] Billing implemented.
- [x] Delivery Areas and People implemented.
- [x] Delivery Dockets and Reporting implemented.
- [x] Publication Wave 1 integrated.
- [x] Orders and Holidays Wave 1 integrated.
- [x] MainMenu integrated.
- [x] Demonstration data prepared.
- [x] Integration PR #8 merged into master.
- [ ] Integrate and verify Patrick's Wave 2.
- [ ] Integrate and verify Dylan's Wave 2.
- [ ] Complete final JUnit regression testing.
- [ ] Complete final end-to-end UAT.
- [ ] Add remaining test evidence and final documentation.
- [ ] Build and test the executable JAR.
- [ ] Prepare the group submission ZIP.
- [ ] Submit the group assignment.
- [ ] Submit individual reflections.

---

**Last reviewed: 19 November 2026**
