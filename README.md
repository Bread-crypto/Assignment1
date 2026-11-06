# News Agent Delivery System

Software Engineering 3 – Team Assignment (TUS, 2026)

A console application that helps a newsagent manage newspaper and magazine deliveries in a local area: customers, orders, publications and stock, delivery areas, daily delivery dockets and monthly bills.

---

## 📌 Now: Sprint 3 (Week 9–10, 2 – 13 Nov)

1. **Wave 1 carry-over:** Publication (Dylan) and Orders (Patrick) need a pull request open by **Sun 8 Nov, 23:59**. Billing and dockets depend on them.
2. **Wave 2:** everyone starts **now**. Don't wait for the other branches:
   - `Team → Switch To → master → Pull → New Branch… feature/<area>`
   - When the missing Wave 1 PRs are merged: `Team → Fetch from origin → Merge → origin/master` on your branch.
3. Use table and column names **exactly** as listed below, so code written in parallel fits together.

---

## Sprint plan

Scrum with one sprint per 1–3 weeks. The **sprint review** happens at the weekly practical with Mike, and the next week's workload is agreed there.

| Sprint | Weeks | Dates | Length | Goal | Status |
|---|---|---|---|---|---|
| 0 | 2–3 | 14 Sep – 25 Sep | 2 weeks | SDLC justification, team repo, interview questions for the newsagent | ✅ Done |
| 1 | 4 | 28 Sep – 2 Oct | 1 week | 32 user stories + acceptance criteria, project plan | ✅ Done |
| 2 | 5–7 | 5 Oct – 23 Oct | 3 weeks | TDD example (Login), **Wave 1** – core entities up to CLI | ⚠️ 2 of 4 entities done |
| – | 8 | 26 Oct – 30 Oct | – | Reading week (no sprint) | – |
| **3** | **9–10** | **2 Nov – 13 Nov** | **2 weeks** | **Finish Wave 1 + Wave 2** – dockets, deliveries, billing | 🔄 In progress |
| 4 | 11 | 16 Nov – 20 Nov | 1 week | Integration: one main menu, foreign keys, UAT, bug fixes | ⏳ Planned |
| 5 | 12 | 23 Nov – 27 Nov | 1 week | Final testing, documentation, individual reflections, submission | ⏳ Planned |

### Pipeline – the same for every story (Mike's Customer example)

```
User story → Test cases (Test Design Template) → Class skeleton → JUnit tests (RED)
→ Implement (GREEN) → Refactor → SQL table → DAO → CLI menu → UAT
```

---

## Sprint 2 – Wave 1: core entities (17 stories)

| Who | Entity | Stories | Table + columns | Status |
|---|---|---|---|---|
| Rodion | Customer | 1, 26, 7 | `CustomerTable`: customerId, firstName, surname, address, phoneNumber, deliveryAreaId, status | ✅ Done (26 JUnit tests, DAO, CLI, UAT) |
| Ahmed | Delivery Area + Delivery Person | 17, 32, 12 | `DeliveryAreaTable`: deliveryAreaId, areaName, deliveryPersonId<br>`DeliveryPersonTable`: deliveryPersonId, firstName, surname, phoneNumber, status | ✅ Done |
| Dylan | Publication + stock | 27, 29, 31, 8, 9, 10 | `PublicationTable`: publicationId, publicationName, publicationType, publicationPrice, stockQuantity, lowStockThreshold, publicationFrequency, releaseDay | ⚠️ Carried over → PR by Sun 8 Nov |
| Patrick | Orders + holidays | 2, 5, 6, 4, 3 | `OrderTable`: orderId, customerId, publicationId, deliveryDays, orderStartDate, orderEndDate<br>`HolidayTable`: holidayId, customerId, holidayStartDate, holidayEndDate | ⚠️ Carried over → PR by Sun 8 Nov |

**Sprint 2 retrospective:** two of the four entities were not finished on time.
- **Cause:** work started late and nobody raised a blocker.
- **Change for Sprint 3:**
  - every member pushes at least once by **Wednesday** of each week;
  - anyone stuck says so in the group chat on the **same day**.

---

## Sprint 3 – Week 9–10: Wave 2 (15 stories) – 2 Nov to 13 Nov

Estimates are in hours of work per person and are based on the number of acceptance criteria per story.

| Who | Area | Stories | Depends on | Estimate | Deadline |
|---|---|---|---|---|---|
| Rodion | Billing | 19, 20, 21, 22, 28 (25 Login ✅) | Customer ✅, Orders, Publication price | ~14 h | Fri 13 Nov |
| Ahmed | Delivery dockets + daily summary | 11, 16, 18, 30 | Delivery Area ✅, Orders, Customer ✅ | ~11 h | Fri 13 Nov |
| Dylan | Stock on dockets + delivery history | 13, 24, 23 | Publication, dockets | ~8 h (+ Wave 1 finish) | Fri 13 Nov |
| Patrick | Recording deliveries | 14, 15 | dockets | ~6 h (+ Wave 1 ~12 h) | Fri 13 Nov |

### Order of work inside the sprint

| Days | Step | Time |
|---|---|---|
| Mon 9 – Tue 10 Nov | Test Design for your stories + class skeleton + JUnit tests → **screenshot RED** → commit | ~3 h |
| Wed 11 Nov | Implement method by method → **screenshot GREEN** → refactor → commit + push | ~3 h |
| Thu 12 Nov | `CREATE TABLE` in `database/newsagent_db.sql` + DAO | ~3 h |
| Fri 13 Nov | CLI menu + UAT notes → pull request | ~2 h |

Steps that need someone else's table (for example a bill needs orders and prices) are written against the column names in this README. They are tested once that PR is merged.

---

## Sprint 4 – Week 11: Integration (16 Nov – 20 Nov)

| Task | Owner | Estimate |
|---|---|---|
| One main menu (`MainMenu.java`) that opens every entity's CLI | Rodion | 3 h |
| Foreign keys in `newsagent_db.sql` (Customer → DeliveryArea, Order → Customer/Publication, …) | Ahmed | 2 h |
| UAT for your own stories → `tests/<Entity> UAT results.txt` | Everyone | 2 h each |
| Bug fixes from UAT, all JUnit tests green on `master` | Everyone | 2 h each |

## Sprint 5 – Week 12: Finish (23 Nov – 27 Nov)

| Task | Owner | Estimate |
|---|---|---|
| Final full regression test (all JUnit + UAT) | Everyone | 2 h |
| Update README, test documents, user stories status | Rodion | 2 h |
| Individual reflection | Everyone | 2 h each |
| Final demo to Mike + submission | Everyone | – |

---

## Architecture – layered (Week 9/10 lecture)

Each entity is split into the same layers. Every layer talks only to the layer below it.

| Layer | Class (example) | Job |
|---|---|---|
| Presentation | `CustomerCLI` | Menu, reads input, prints results and errors |
| Business | `Customer` | Fields + validation rules (`verify…` methods), tested with JUnit |
| Persistence | `CustomerDAO` | SQL: insert, find, update, close – uses `PreparedStatement` |
| Database | `DatabaseConnection` + MySQL `newsagent_db` | Connection details and tables |

---

## Risks

| Risk | Likelihood | Impact | What we do |
|---|---|---|---|
| A member finishes late, others are blocked | High (happened in Sprint 2) | High | Agreed column names, so work runs in parallel; push by Wednesday; blockers raised the same day |
| Merge conflicts in shared files (`newsagent_db.sql`, README) | Medium | Medium | Each member edits only their own section; pull before every new branch |
| Different local DB setup | Medium | Low | One shared SQL script; password only changed locally |
| Lost work | Low | High | Commit after every TDD step, push daily |

---

## Team

| Name | Wave 1 | Wave 2 |
|---|---|---|
| Rodion Omelich | Customer, DB setup, repo | Billing, main menu |
| Dylan Kirwan | Publication + stock | Stock on dockets, delivery history |
| Ahmed Sawan | Delivery Area + Delivery Person | Delivery dockets, daily summary |
| Patrick Byrne | Orders + holidays | Recording deliveries |

## What the system does

| Area | Features |
|---|---|
| **Customers** | Add, search, update and close customer accounts; holidays |
| **Orders** | One or more publications per customer, on chosen days, with start/end dates |
| **Publications & Stock** | Name, type, frequency, price and quantity in stock; low-stock warning |
| **Deliveries** | Daily delivery docket per delivery area (24 areas); each delivery marked delivered / missed |
| **Billing** | Monthly bill per customer listing each delivery by date and price, with a total; payments |

## Development approach

- **SDLC:** Agile Scrum (sprints above), with V-Model test front-loading: tests are designed from acceptance criteria before coding.
- **TDD:** tests written first, then code (see `Login` / `LoginTest` – User Story 25, and `Customer` / `CustomerTest`).
- **Spec:** `User Stories - Newsagent (32).xlsx`. Every attribute and rule becomes a DB column, a Java field, a validation method and a test case.

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
3. Branch `master` → `Finish`. Use `eclipse-workspace` as your workspace, **not** the git folder.

### 2. Libraries
- **JUnit 5:** right-click project → `Build Path → Add Libraries… → JUnit → JUnit 5`
- **MySQL driver:** right-click `lib/mysql-connector-j-26.7.0.jar` → `Build Path → Add to Build Path`

### 3. Database
1. Install **MySQL Server 8 + MySQL Workbench**. Root password: `root`.
2. In Workbench: `File → Open SQL Script…` → `database/newsagent_db.sql` → run ⚡.
3. Each of us has our own local copy of the DB. The **structure** is shared through this script, not the data.
4. If your root password is different, change `PASSWORD` in `DatabaseConnection.java` **locally only** – don't commit it.

### 4. Before your first push
Create a **Personal Access Token**: GitHub → Settings → Developer settings → Personal access tokens → **Tokens (classic)** → Generate → tick `repo`.
In Eclipse, enter your GitHub username + the token as the password. If push says `git-receive-pack not permitted`, Eclipse has no credentials. Fix it under `Git Repositories → Remotes → origin → Configure Push → Change… → Authentication`.

---

## How we work with Git

**Never commit directly to `master`.**

```
1. Team → Switch To → master → Team → Pull       (always start from the latest master)
2. Team → Switch To → New Branch…                (feature/<area>, e.g. feature/billing)
3. Write tests + code, commit after each TDD step (red → green → refactor)
4. Team → Push Branch                             (at least once a week, by Wednesday)
5. GitHub: Compare & pull request → a teammate reviews and merges
6. Need code merged after you branched? Team → Fetch from origin → Merge → origin/master
```

**Never commit:** `bin/`, `*.class`, `.classpath`, `.project`, `.settings/` (they are in `.gitignore`).

| Branch type | Example |
|---|---|
| Entity / feature | `feature/customer`, `feature/billing` |
| Bug fix | `fix/bill-total-rounding` |
| Docs / setup | `docs/sprint-plan`, `setup/database` |

Commit messages – short, with the story number: `US-19: calculate monthly bill total`

---

## Project status

- [x] Sprint 0 – SDLC justification, repository, newsagent questions
- [x] Sprint 1 – 32 user stories (Mike's format), project plan
- [x] TDD example – Login (User Story 25), 14 tests passing
- [x] Database `newsagent_db` + shared SQL script
- [x] Wave 1 – Customer (Rodion), Delivery Area/Person (Ahmed)
- [ ] Wave 1 – Publication (Dylan), Orders (Patrick) – PR by Sun 8 Nov
- [ ] Sprint 3 – Wave 2: dockets, deliveries, billing (by Fri 13 Nov)
- [ ] Sprint 4 – integration, main menu, UAT (week 11)
- [ ] Sprint 5 – final testing, documentation, reflections (week 12)
