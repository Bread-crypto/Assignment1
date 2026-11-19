# News Agent Delivery System

**Software Engineering 3 — Team Assignment | TUS Athlone | 2026**

A Java console application for managing a local newsagent's customers, publications, subscriptions, delivery areas, daily delivery dockets and monthly billing.

The project uses Java, Eclipse, JUnit 5, MySQL and GitHub, following an Agile Scrum development approach with Test-Driven Development (TDD).

## 1. Project Overview

The News Agent Delivery System is designed to support the daily operations of a local newspaper and magazine delivery business.

The application provides functionality for:

- Customer account management.
- Newspaper and magazine management.
- Stock and publication price management.
- Customer subscriptions and holidays.
- Delivery areas and delivery people.
- Daily delivery dockets and reporting.
- Monthly billing and payment recording.
- Outstanding balance management.

The application uses a shared MySQL database and a common console interface.

### Project Status

The core application modules have been developed across two development waves.

The integrated application was merged into `master` through Pull Request #8 on 19 November 2026.

The final stage focuses on combining the latest contributions, verifying database compatibility, completing regression testing and preparing the group submission.

Demonstration data is available to support testing of the main business workflow.

## 2. Team Responsibilities

| Team Member | Wave 1 | Wave 2 | Additional Responsibility |
|---|---|---|---|
| Rodion Omelich | Customer Management (US 1, 7, 26) | Billing (US 19, 20, 21, 22, 28) | MainMenu and integration |
| Ahmed Sawan | Delivery Areas and People (US 12, 17, 32) | Delivery Dockets and Reporting (US 11, 16, 18, 30) | Delivery integration |
| Patrick Byrne | Orders and Holidays (US 2, 3, 4, 5, 6) | Recording Deliveries (US 14, 15) | Delivery records |
| Dylan Kirwan | Publications and Stock (US 8, 9, 10, 27, 29, 31) | Stock on Dockets and Delivery History (US 13, 23, 24) | Stock management |

Each team member was responsible for implementing, testing and integrating their assigned functionality.

Development was coordinated using GitHub feature branches, commits and pull requests.

## 3. Application Architecture

The system follows a layered architecture that separates user interaction, business logic and database operations.

| Layer | Examples | Responsibility |
|---|---|---|
| Presentation | MainMenu, CustomerCLI, BillCLI, DeliveryWave2CLI | User input, menus and output |
| Business / Domain | Customer, Bill, Publication, DeliveryDocketGenerator | Validation, business rules and calculations |
| Persistence | CustomerDAO, BillDAO, OrderDAO, PublicationDAO | Database operations using JDBC |
| Database | MySQL newsagent_db | Data storage and relationships |

### Architecture Overview

**MainMenu → CLI Modules → Domain Classes / DAO → MySQL Database**

The application uses domain classes for validation and business rules, while DAO classes manage persistence.

For example, when generating a monthly bill:

1. BillCLI collects the customer ID and billing period.
2. Bill validates the billing information.
3. BillDAO retrieves the relevant delivery records and prices.
4. Bill calculates the total.
5. BillDAO saves the bill to MySQL.
6. BillCLI displays the result and exports the bill as a text file.

This separation improves maintainability, testability and code reuse.

## 4. Main Menu

The application starts from:

`Assignment1/src/MainMenu.java`

The integrated main menu contains:

| Option | Module |
|---|---|
| 1 | Customers |
| 2 | Publications & Stock |
| 3 | Orders & Holidays |
| 4 | Delivery Areas & Delivery People |
| 5 | Delivery Dockets & Daily Reports |
| 6 | Billing |
| 0 | Exit |

MainMenu provides a single entry point and connects the individual CLI modules.

The login component validates the username and password format before displaying the main menu.

## 5. Wave 1 — Core Entities

### Customer Management

Files:

- Customer.java
- CustomerDAO.java
- CustomerCLI.java
- CustomerTest.java

Features include creating customers, searching by ID or surname, listing customers, updating information and closing accounts.

Customer validation rules include:

- First name: 1–50 characters.
- Surname: 1–50 characters.
- Address: 1–100 characters.
- Phone number: exactly 10 digits, starting with 0.
- Delivery area: ID between 1 and 24.

Customers have ACTIVE or CLOSED status.

Closing an account preserves historical information and existing database relationships.

### Publication Management

The publication module supports:

- Adding newspapers and magazines.
- Finding and listing publications.
- Updating stock quantities.
- Changing publication prices.
- Managing publication frequency and release days.
- Removing publications where permitted.

### Orders and Holidays

The order management module supports:

- Creating customer subscriptions.
- Viewing customer orders.
- Ending subscriptions.
- Recording holiday periods.
- Viewing customer holidays.

Orders connect customers with publications and define delivery schedules.

### Delivery Areas and People

The delivery management module supports:

- Adding delivery people.
- Viewing and updating delivery people.
- Managing active and inactive statuses.
- Creating and updating delivery areas.
- Assigning delivery people to delivery areas.

Customers are connected to delivery areas using `deliveryAreaId`.

## 6. Wave 2 — Business Operations

### Monthly Billing

Files:

- Bill.java
- BillItem.java
- BillDAO.java
- BillCLI.java
- BillTest.java

Billing supports:

- Generating monthly bills for individual customers.
- Generating itemised bills.
- Exporting bills to text files.
- Recording payments.
- Displaying unpaid bills.
- Calculating outstanding balances.
- Generating bills for all active customers.

Bills are calculated using delivery records marked `DELIVERED`.

Records marked `MISSED` or `OUT OF STOCK` are excluded.

Publication prices are obtained from the price history applicable on the delivery date.

The application prevents duplicate bills for the same customer and billing period.

Payments change the bill status from UNPAID to PAID.

Exported bills use the format:

`bill_<customerId>_<yyyy-mm>.txt`

### Delivery Dockets and Reporting

The docket and reporting functionality supports:

- Generating daily delivery dockets.
- Filtering by delivery date and area.
- Checking customer and order status.
- Checking publication schedules.
- Excluding customers on holiday.
- Displaying delivery results and summaries.
- Printing and saving delivery dockets.

### Delivery Recording

The delivery recording functionality manages delivery outcomes, including:

- DELIVERED
- MISSED
- OUT OF STOCK

Missed deliveries can include a reason.

Delivery records are stored in the database and used by reporting and billing functionality.

### Stock and Delivery History

Stock management and delivery history form part of the overall delivery workflow.

The final integration stage includes verifying stock movements, delivery status handling and the relationships between docket data and recorded delivery outcomes.

## 7. Database Design

The application uses MySQL.

Database scripts:

- `database/newsagent_db.sql`
- `database/demo_data.sql`

Main tables include:

| Table | Purpose |
|---|---|
| CustomerTable | Customer details and account status |
| publication | Publication information, prices and stock |
| publication_price_history | Historical publication prices |
| OrderTable | Customer subscriptions |
| HolidayTable | Customer holiday periods |
| DeliveryAreaTable | Delivery areas |
| DeliveryPersonTable | Delivery people |
| DeliveryDocketTable | Daily delivery dockets |
| DeliveryTable | Delivery records |
| DeliveryDocketLineTable | Individual docket lines |
| StockHistoryTable | Stock movements |
| BillTable | Monthly bills and payments |

Foreign keys connect related records and maintain referential integrity.

The application accesses MySQL using JDBC and DAO classes.

## 8. Development Methodology

### Agile Scrum

Agile Scrum was selected to support collaborative development.

The team organised work into sprints, reviewed progress and adjusted tasks based on project requirements and integration needs.

### Sprint Plan

| Sprint | Dates | Objective |
|---|---|---|
| Sprint 0 | 14–25 September | SDLC justification, repository and requirements gathering |
| Sprint 1 | 28 September–2 October | User stories, acceptance criteria and project planning |
| Sprint 2 | 5–23 October | TDD and Wave 1 development |
| Sprint 3 | 2–13 November | Wave 1 completion and Wave 2 development |
| Sprint 4 | 16–20 November | Integration, testing and group submission |
| Sprint 5 | 23–27 November | Individual reflection |

### Test-Driven Development

The project follows the lecturer's TDD process:

**User Story → Acceptance Criteria → Test Design → Class Skeleton → JUnit RED → Implementation GREEN → Refactor → DAO → CLI → UAT**

The process helps ensure that functionality is implemented against defined requirements.

### Billing TDD Evidence

The Billing implementation includes separate commits demonstrating the TDD process.

| Commit | Description |
|---|---|
| 59143a4 | Billing Test Design |
| f1232f3 | Bill skeleton and failing tests (RED) |
| 85e08db | Billing implementation (GREEN) |
| 8021a39 | BillTable, BillDAO and BillCLI |

This provides evidence of the test-first development sequence.

## 9. Git and Team Integration

GitHub was used for collaborative development and version control.

Each team member worked on assigned functionality using separate branches.

The integration process involved:

1. Fetching the latest changes.
2. Reviewing feature branches.
3. Combining compatible implementations.
4. Resolving merge conflicts.
5. Connecting CLI modules through MainMenu.
6. Checking database compatibility.
7. Running regression and integration tests.
8. Merging verified changes into master.

The shared application was integrated through Pull Request #8 on 19 November 2026.

**Repository:** https://github.com/Bread-crypto/Assignment1

## 10. Running the Application

### Requirements

- Eclipse IDE for Java Developers.
- Java JDK (the committed Eclipse project settings specify Java 23).
- MySQL Server.
- MySQL Workbench.
- JUnit 5.
- MySQL Connector/J.

### Installation

1. Clone the repository:

   `https://github.com/Bread-crypto/Assignment1.git`

2. Import the Java project from the inner `Assignment1` directory into Eclipse.

3. Configure the project JDK.

4. Add the MySQL JDBC driver and JUnit 5 to the Build Path.

5. Open MySQL Workbench.

6. Execute `database/newsagent_db.sql`.

7. For a fresh demonstration database, execute `database/demo_data.sql`.

8. Check the local MySQL connection settings in `Assignment1/src/DatabaseConnection.java`.

9. Run `MainMenu.java` as a Java Application.

### Login Validation

- Username: 6–12 characters.
- Password: 4–6 digits.
- Maximum attempts: 3.

The current implementation validates input format rather than authenticating against stored user accounts.

### Demonstration Data

The demonstration database contains customers, delivery areas, delivery people, publications, subscriptions, holidays and example delivery records.

For Billing demonstrations, use October 2026.

Example:

- Customer ID: 1
- Billing Year: 2026
- Billing Month: 10

With the original demonstration data, John Murphy's expected bill is €11.60.

If the bill has already been generated, the system prevents creating a duplicate.

## 11. Testing and Quality Assurance

JUnit 5 is used for automated testing.

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

Additional test classes cover order, holiday and delivery functionality.

### Test Design and UAT

Test documentation includes:

- `tests/Bill Test Design.docx`
- `tests/UAT.txt`

JUnit verifies individual methods, validation rules and business logic.

User Acceptance Testing verifies application behaviour through the console.

Final regression testing is carried out against the integrated version of the system.

Testing results should be recorded after the final integration.

## 12. End-to-End Business Workflow

The intended application workflow is:

**Create Customer → Create Subscription → Generate Daily Docket → Allocate Stock → Record Delivery → Generate Monthly Bill → Record Payment → View Outstanding Balance**

The shared database allows the modules to exchange information.

Demonstration data is available for testing the business workflow, including delivered, missed and out-of-stock examples.

Final integration testing verifies the interactions between the individual modules.

## 13. Technical Considerations

### Database Configuration

The database connection uses local demonstration credentials.

For a production environment, credentials should be stored in external configuration rather than source code.

### Authentication

The current login component validates input format.

A production version would require secure credential storage and actual user authentication.

### Database Schema

The shared SQL script uses `CREATE TABLE IF NOT EXISTS`.

This creates missing tables but does not automatically migrate existing database structures.

A fresh database is recommended for repeatable demonstration testing.

### Integration Verification

The final verification process includes checking Java compilation, database compatibility, automated tests and end-to-end CLI functionality.

## 14. Assignment Submission

### Group Submission

**Deadline: Friday, 20 November 2026 — Week 11**

Worth 40% of the module assessment.

One ZIP submission is required per group.

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

Each team member submits an individual reflection.

## 15. Final Submission Checklist

- [x] Shared GitHub repository established.
- [x] Project plan and user stories created.
- [x] Core Wave 1 modules developed.
- [x] Billing module implemented.
- [x] Delivery Dockets and Reporting implemented.
- [x] MainMenu integrated.
- [x] Shared database structure prepared.
- [x] Demonstration data prepared.
- [x] Integration PR #8 merged into master.
- [ ] Verify final Wave 2 integration.
- [ ] Complete final regression testing.
- [ ] Complete end-to-end UAT.
- [ ] Confirm final documentation and test evidence.
- [ ] Build and test the executable JAR.
- [ ] Prepare the group submission ZIP.
- [ ] Submit the group assignment.
- [ ] Submit individual reflections.

---

**Last reviewed: 19 November 2026**

This README documents the project's structure, development approach, team responsibilities and integration process. Final test results and submission status should be updated after the last integration and verification.
