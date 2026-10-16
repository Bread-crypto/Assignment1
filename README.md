# News Agent Delivery System

Software Engineering 3 – Team Assignment (TUS, 2026)

A console application that helps a newsagent manage newspaper and magazine deliveries in a local area: customers, orders, publications and stock, delivery areas, daily delivery dockets and monthly bills.

---

## 📌 Current plan – Wave 1 due **Friday 23 October** (up to CLI)

**Our spec is `User Stories - Newsagent (32).xlsx`** (Mike's format). Every attribute and rule in it becomes a DB column, a Java field, a validation method and a test case. Use the attribute names **exactly** as written there.

### Pipeline – everyone follows the same steps (as in Mike's Customer example)

```
User story → Test cases (Test Design Template) → Class skeleton → JUnit tests (FAIL)
→ Implement (PASS) → Refactor → SQL table → DAO → CLI menu
```

### Wave 1 – core entities (17 stories) – by Fri 23 Oct

| Who | Entity | Stories | Table + columns |
|---|---|---|---|
| Rodion | Customer | 1, 26, 7 | `CustomerTable`: customerId, firstName, surname, address, phoneNumber, deliveryAreaId, status |
| Dylan | Publication + stock | 27, 29, 31, 8, 9, 10 | `PublicationTable`: publicationId, publicationName, publicationType, publicationPrice, stockQuantity, lowStockThreshold, publicationFrequency, releaseDay |
| Ahmed | Delivery Area + Delivery Person | 17, 32, 12 | `DeliveryAreaTable`: deliveryAreaId, areaName, deliveryPersonId<br>`DeliveryPersonTable`: deliveryPersonId, firstName, surname, phoneNumber, status |
| Patrick | Orders + holidays | 2, 5, 6, 4, 3 | `OrderTable`: orderId, customerId, publicationId, deliveryDays, orderStartDate, orderEndDate<br>`HolidayTable`: holidayId, customerId, holidayStartDate, holidayEndDate |

### Wave 2 – built on Wave 1 (15 stories) – weeks 9–10

| Who | Area | Stories |
|---|---|---|
| Rodion | Billing | 19, 20, 21, 22, 28 (25 Login – done) |
| Dylan | Stock on dockets + delivery history | 13, 24, 23 |
| Ahmed | Delivery dockets + daily summary | 11, 16, 18, 30 |
| Patrick | Recording deliveries | 14, 15 |

### Checklist for your entity (Wave 1)

- [ ] Test cases for your stories in the Test Design Template
- [ ] Class skeleton + JUnit tests → run → **screenshot red**
- [ ] Implement methods one by one → **screenshot green** → refactor
- [ ] `CREATE TABLE` added to `database/newsagent_db.sql`
- [ ] DAO class (add, find by id, get all, update, delete)
- [ ] CLI menu for your entity
- [ ] Pull request opened, reviewed by a teammate, merged

**Rodion pushes the full Customer chain first – copy that pattern for your entity.**

---

## What the system does

| Area | Features |
|---|---|
| **Customers** | Add, search, update and close customer accounts; holidays |
| **Orders** | One or more publications per customer, on chosen days, with start/end dates |
| **Publications & Stock** | Name, type, frequency, price and quantity in stock; low-stock warning |
| **Deliveries** | Daily delivery docket per delivery area (24 areas); each delivery marked delivered / missed |
| **Billing** | Monthly bill per customer listing each delivery by date and price, with a total; payments |

---

## Team

| Name | Role (Wave 1) |
|---|---|
| Rodion Omelich | Customer, DB setup, repo |
| Dylan Kirwan | Publication + stock |
| Ahmed Sawan | Delivery Area + Delivery Person |
| Patrick Byrne | Orders + holidays |

---

## Development approach

- **SDLC:** Agile Scrum, with V-Model test front-loading (tests are designed from acceptance criteria before coding).
- **TDD:** tests written first, then code (see `Login.java` / `LoginTest.java` – User Story 25).
- **Progress:** reviewed weekly at the practical with Mike.

## Tech stack

| Area | Tool |
|---|---|
| Language / IDE | Java, Eclipse |
| Testing | JUnit 5 |
| Database | MySQL 8 + MySQL Workbench |
| DB driver | `lib/mysql-connector-j-26.7.0.jar` |
| Version control | Git + GitHub |

---

## Getting started

### 1. Clone the project into Eclipse
1. `File → Import… → Git → Projects from Git (with smart import)`
2. `Clone URI` → `https://github.com/Bread-crypto/Assignment1.git`
3. Leave User/Password empty → `Next` → branch `master` → `Finish`

### 2. Libraries
- **JUnit 5:** right-click project → `Build Path → Add Libraries… → JUnit → JUnit 5`
- **MySQL driver:** right-click `lib/mysql-connector-j-26.7.0.jar` → `Build Path → Add to Build Path`

### 3. Database
1. Install **MySQL Server 8 + MySQL Workbench** (MySQL Installer for Windows → Custom). Root password: `root`.
2. In Workbench: `File → Open SQL Script…` → `database/newsagent_db.sql` → run ⚡.
3. Each of us has our own local copy of the DB – the **structure** is shared through this script, not the data.
4. If your root password is different, change `PASSWORD` in `DatabaseConnection.java` **locally only** – don't commit it.

### 4. Before your first push
GitHub doesn't accept your normal password from Eclipse. Create a **Personal Access Token**: GitHub → Settings → Developer settings → Personal access tokens → Tokens (classic) → Generate → tick `repo`. Paste it as the password when Eclipse asks.

---

## How we work with Git

**Never commit directly to `master`.**

```
1. Team → Pull                       (always first)
2. Team → Switch To → New Branch…    (e.g. feature/publication)
3. Write tests + code, commit after each TDD step
4. Team → Commit… → Commit and Push
5. GitHub: Compare & pull request → a teammate reviews and merges
6. After merge: Team → Switch To → master → Team → Pull
```

**Never commit:** `bin/`, `*.class`, `.classpath`, `.project`, `.settings/` (they are in `.gitignore`).

| Branch type | Example |
|---|---|
| Entity / feature | `feature/customer`, `feature/publication` |
| Bug fix | `fix/bill-total-rounding` |
| Docs / setup | `docs/user-stories`, `setup/database` |

Commit messages – short, with the story number: `US-1: validate customer phone number (10 digits)`

---

## Project status

- [x] Repository, README, user stories (32, Mike's format)
- [x] TDD example – Login (User Story 25), 14 tests passing
- [x] Database `newsagent_db` + `CustomerTable`
- [ ] Wave 1 – Customer, Publication, Delivery Area/Person, Orders (due Fri 23 Oct)
- [ ] Wave 2 – Dockets, deliveries, billing (weeks 9–10)
- [ ] Integration – one main menu, UAT, bug fixes (week 11)
- [ ] Final testing, documentation, individual reflections (week 12)
