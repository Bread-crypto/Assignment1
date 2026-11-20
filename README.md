
# News Agent Delivery System

**Software Engineering 3 — Team Assignment | TUS Athlone | 2026**

A Java console application for managing a local newsagent's customers, publications, subscriptions, delivery areas, daily delivery dockets, stock and monthly billing.

The project uses Java, Eclipse, JUnit 5, MySQL and GitHub, following an Agile Scrum development approach with Test-Driven Development (TDD).

## 1. Project Overview

The News Agent Delivery System supports the daily operations of a local newspaper and magazine delivery business.

The application provides functionality for:

- Customer account management.
- Newspaper and magazine management.
- Stock and publication price management.
- Customer subscriptions and holidays.
- Delivery areas and delivery people.
- Daily delivery dockets and reporting.
- Recording completed and missed deliveries.
- Stock issuing and delivery history.
- Monthly billing and payment recording.
- Outstanding balance management.

The application uses a shared MySQL database and a common console interface.

### Final Project Status

The project was developed across two main development waves, followed by a final integration stage.

The initial integration was merged into `master` through Pull Request #8 on 19 November 2026.

The final integration was completed through [Pull Request #9](https://github.com/Bread-crypto/Assignment1/pull/9) on 20 November 2026.

The integrated application contains the modules developed by all four team members.

**Final verification:**

- Java compilation completed successfully.
- 226 JUnit tests passed.
- 0 test failures and 0 errors.
- All eight MainMenu sections integrated.
- Customer, Billing and Delivery operations tested with MySQL.
- Delivery history and out-of-stock reporting verified.
- Stock issuing tested with corresponding database records.
- Runnable JAR built and successfully launched.
- Group submission prepared and submitted through Moodle.

## 2. Team Responsibilities

| Team Member | Wave 1 | Wave 2 | Additional Responsibility |
|---|---|---|---|
| Rodion Omelich | Customer Management (US 1, 7, 26) | Billing (US 19, 20, 21, 22, 28) | MainMenu and final integration |
| Ahmed Sawan | Delivery Areas and People (US 12, 17, 32) | Delivery Dockets and Reporting (US 11, 16, 18, 30) | Delivery integration |
| Patrick Byrne | Orders and Holidays (US 2, 3, 4, 5, 6) | Recording Deliveries (US 14, 15) | Delivery records |
| Dylan Kirwan | Publications and Stock (US 8, 9, 10, 27, 29, 31) | Stock on Dockets and Delivery History (US 13, 23, 24) | Stock management |

Each team member developed and tested their assigned functionality.

GitHub branches, commits and pull requests were used to manage the development and integration process.

## 3. Application Architecture

The system follows a layered architecture that separates user interaction, business rules and database operations.

| Layer | Examples | Responsibility |
|---|---|---|
| Presentation | MainMenu, CustomerCLI, BillCLI, DeliveryCLI, DeliveryWave2CLI, DylanWave2CLI | User input, menus and output |
| Business / Domain | Customer, Bill, Publication, DeliveryDocketGenerator, DocketStockService | Validation, business rules and calculations |
| Persistence | CustomerDAO, BillDAO, OrderDAO, DeliveryDAO, StockHistoryDAO | Database operations using JDBC |
| Database | MySQL newsagent_db | Data storage and relationships |

### Architecture Overview

```text
                     MainMenu.java
                           |
                    CLI Modules
                           |
                Domain Classes / DAO
                           |
                 DatabaseConnection
                           |
                     MySQL Database
```

The application separates responsibilities between classes.

CLI classes handle user interaction, domain classes implement validation and business rules, and DAO classes perform SQL operations.

For example, when generating a monthly bill:

1. BillCLI collects the customer ID and billing period.
2. Bill validates the billing information.
3. BillDAO retrieves delivery records and prices.
4. Bill calculates the total.
5. BillDAO saves the bill to MySQL.
6. BillCLI displays and exports the result.

This separation makes the code easier to understand, maintain, test and integrate.

## 4. Main Menu

The application starts from:

`Assignment1/src/MainMenu.java`

The final integrated main menu contains:

```text
========== NEWS AGENT DELIVERY SYSTEM ==========

1. Customers
2. Publications & Stock
3. Orders & Holidays
4. Delivery Areas & Delivery People
5. Delivery Dockets & Daily Reports
6. Billing
7. Record Deliveries
8. Stock and Delivery History
0. Exit
```

MainMenu provides a single entry point and connects the individual CLI modules.

The login component validates username and password format before displaying the main menu.

Each submenu allows the user to return to MainMenu.

## 5. Wave 1 — Core Entities

### Customer Management

Main files:

- Customer.java
- CustomerDAO.java
- CustomerCLI.java
- CustomerTest.java

Features include:

- Creating customers.
- Finding customers by ID.
- Searching by surname.
- Listing all customers.
- Updating customer details.
- Closing customer accounts.

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

- Adding and updating delivery people.
- Managing active and inactive statuses.
- Creating and updating delivery areas.
- Assigning delivery people to delivery areas.

Customers are connected to delivery areas using `deliveryAreaId`.

## 6. Wave 2 — Business Operations

### Monthly Billing

Main files:

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

Records marked `MISSED` or `OUT OF STOCK` are excluded from monthly charges.

Historical publication prices are selected based on the delivery date.

The application prevents duplicate bills for the same customer and billing period.

Payments change the bill status from UNPAID to PAID.

Exported bills use the format:

`bill_<customerId>_<yyyy-mm>.txt`

### Delivery Dockets and Reporting

Main classes include:

- DeliveryDocket.java
- DeliveryDocketDAO.java
- DeliveryDocketGenerator.java
- DeliveryWave2Service.java
- DeliveryWave2CLI.java

Features include:

- Generating daily delivery dockets.
- Filtering deliveries by date and area.
- Checking customer and order status.
- Checking publication release schedules.
- Excluding customers on holiday.
- Displaying daily delivery summaries.
- Printing and saving delivery dockets.
- Issuing stock for a selected docket.

### Delivery Recording

Main files:

- Delivery.java
- DeliveryDAO.java
- DeliveryCLI.java
- DeliveryTest.java
- DeliveryDAOTest.java

Delivery Recording supports:

- Recording completed deliveries.
- Recording missed deliveries with reasons.
- Recording out-of-stock delivery outcomes.
- Saving records to DeliveryTable.
- Retrieving completed delivery history.

Delivery records are shared with the Billing and Reporting modules.

### Stock and Delivery History

Main files:

- DocketStockService.java
- StockHistoryDAO.java
- DeliveryHistoryDAO.java
- DeliveryHistoryEntry.java
- DylanWave2CLI.java

Features include:

- Issuing publication stock for delivery dockets.
- Recording stock movements.
- Identifying out-of-stock publications.
- Viewing customer delivery history.
- Viewing customers affected by out-of-stock deliveries.
- Returning eligible undelivered stock through the service layer.

### Stock Integration Test

Stock issuing was tested through the integrated CLI using MySQL.

A single Irish Times delivery was issued for Docket ID 1.

The database confirmed:

| Check | Result |
|---|---|
| DeliveryDocketLineTable | Docket line created |
| StockHistoryTable | ISSUED movement recorded |
| Quantity issued | 1 |
| Previous stock | 40 |
| New stock | 39 |

This verified the interaction between the docket, stock and database components.

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
| DeliveryTable | Recorded delivery results |
| DeliveryDocketLineTable | Individual docket lines |
| StockHistoryTable | Stock movements |
| BillTable | Monthly bills and payments |

Foreign keys connect related records and support referential integrity.

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

This approach connects implementation with defined test cases and acceptance criteria.

### Billing TDD Evidence

The Billing implementation includes separate commits demonstrating the development process.

| Commit | Description |
|---|---|
| 59143a4 | Billing Test Design |
| f1232f3 | Bill skeleton and failing tests (RED) |
| 85e08db | Billing implementation (GREEN) |
| 8021a39 | BillTable, BillDAO and BillCLI |

These commits provide evidence of the test-first development sequence.

## 9. Git and Team Integration

GitHub was used for collaborative development and version control.

Each team member developed their assigned functionality using separate branches.

The integration process involved:

1. Fetching the latest team branches.
2. Reviewing source code and dependencies.
3. Bringing together compatible implementations.
4. Resolving database and class compatibility issues.
5. Connecting the CLI modules through MainMenu.
6. Running JUnit regression tests.
7. Testing the application with MySQL.
8. Building and testing the executable JAR.
9. Merging verified changes into master.

### Integration History

| Pull Request | Description |
|---|---|
| PR #4 | Customer Management |
| PR #5 | Delivery Areas and People |
| PR #7 | Billing |
| PR #8 | Initial system integration |
| PR #9 | Final Delivery, Stock, Billing and Documentation integration |

The final integration was merged into `master` on **20 November 2026** through:

[Pull Request #9 — Final Integration](https://github.com/Bread-crypto/Assignment1/pull/9)

Separate feature branches and commit history remain available for reviewing development progress.

**Repository:** https://github.com/Bread-crypto/Assignment1

## 10. Running the Application

### Requirements

- Eclipse IDE for Java Developers.
- Compatible Java JDK.
- MySQL Server.
- MySQL Workbench.
- JUnit 5.
- MySQL Connector/J.

### Installation

1. Clone the repository:

   `https://github.com/Bread-crypto/Assignment1.git`

2. Import the Java project from the inner `Assignment1` directory into Eclipse.

3. Configure a compatible Java JDK and add the MySQL JDBC driver to the Build Path.

4. Add JUnit 5 to the Build Path for automated testing.

5. Open MySQL Workbench.

6. Execute `database/newsagent_db.sql`.

7. For a fresh demonstration database, execute `database/demo_data.sql`.

8. Check the local MySQL connection settings in `Assignment1/src/DatabaseConnection.java`.

9. Run `MainMenu.java` as a Java Application.

### Running the Executable

A runnable JAR is included at:

`release/NewsagentDeliverySystem.jar`

Run it using:

```bash
java -jar release/NewsagentDeliverySystem.jar
```

The executable was tested with Java and a local MySQL database.

### Login Validation

- Username: 6–12 characters.
- Password: 4–6 digits.
- Maximum attempts: 3.

The current implementation validates input format rather than authenticating against stored user accounts.

### Demonstration Data

The demonstration database includes:

- Customers and delivery areas.
- Delivery people.
- Publications and price history.
- Orders and subscriptions.
- Holiday records.
- Delivered, missed and out-of-stock examples.

For Billing demonstrations, October 2026 can be used.

Example:

```text
Customer ID: 1
Billing Year: 2026
Billing Month: 10
```

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
- DeliveryTest
- DeliveryDAOTest
- OrderDAOTest
- HolidayDAOTest
- PublicationTest

Additional tests cover order, holiday and delivery database functionality.

### Final JUnit Results

The complete integrated JUnit suite was executed in Eclipse on 20 November 2026.

| Metric | Result |
|---|---|
| Tests run | 226 |
| Passed | 226 |
| Errors | 0 |
| Failures | 0 |

### Test Design and UAT

Test documentation is stored in the `tests/` directory:

- `Bill Test Design.docx`
- `Customer Test Design.docx`
- `Billing UAT results.txt`
- `Customer UAT results.txt`
- `System UAT results.txt`

JUnit verifies individual methods, validation rules and business logic.

User Acceptance Testing verifies application behaviour through the console.

The integrated application was also tested manually with MySQL, including delivery recording, billing, delivery history, stock issuing and customer management.

## 12. End-to-End Business Workflow

The intended application workflow is:

**Create Customer → Create Subscription → Generate Daily Docket → Issue Stock → Record Delivery → Generate Monthly Bill → Record Payment → View Outstanding Balance**

The shared MySQL database allows the modules to exchange information.

The integrated application supports the main operations through its CLI modules.

Demonstration data is provided to support repeatable testing.

## 13. Technical Considerations

### Database Configuration

The database connection uses local demonstration credentials.

For production, credentials should be stored in external configuration rather than source code.

### Authentication

The current login component validates input format.

A production version would require secure credential storage and account authentication.

### Database Schema

The shared SQL script uses `CREATE TABLE IF NOT EXISTS`.

This creates missing tables but does not automatically migrate existing database structures.

A fresh database is recommended for repeatable demonstration testing.

### Stock Issuing

Stock issuing uses database transactions to update stock quantities, create docket lines and record stock history.

Repeated issuing of the same docket is not currently blocked by a dedicated application-level safeguard. This is a known limitation for further improvement.

### Further Improvements

Potential improvements include stronger authentication, more automated end-to-end integration tests and additional safeguards for repeated stock issuing.

## 14. Assignment Submission

### Group Submission

**Friday, 20 November 2026 — Week 11**

Worth 40% of the module assessment.

One ZIP submission per group containing:

1. Project Plan
2. Specification
3. Design
4. Source Code
5. Tests
6. System Executable and GitHub Link

The group submission has been prepared and submitted through Moodle.

### Individual Reflection

**Friday, 27 November 2026 — Week 12**

Worth 20% of the module assessment.

Each team member submits their own reflection.

## 15. Final Submission Checklist

- [x] Shared GitHub repository established.
- [x] Project plan and user stories created.
- [x] Core Wave 1 modules integrated.
- [x] Billing module integrated.
- [x] Delivery Dockets and Reporting integrated.
- [x] Delivery Recording integrated.
- [x] Stock and Delivery History modules integrated.
- [x] MainMenu with eight sections completed.
- [x] Shared database structure prepared.
- [x] Demonstration data prepared.
- [x] JUnit regression testing: 226 tests passed.
- [x] MySQL integration scenarios verified.
- [x] Runnable JAR built and tested.
- [x] Final integration PR #9 merged into master.
- [x] Project documentation and UAT evidence included.
- [x] Group assignment submitted through Moodle.
- [ ] Individual reflections — Week 12.

---

**Last updated: 20 November 2026**

This README documents the completed group integration, development process, team responsibilities, test results and submission structure.
