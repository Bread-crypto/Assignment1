# News Agent Delivery System

Software Engineering 3 – Team Assignment (TUS, 2026)

A console application that helps a newsagent manage newspaper and magazine deliveries in a local area: customers, orders, stock, daily delivery dockets and monthly bills.

---

## What the system does

| Area | Features |
|---|---|
| **Customers** | Add, view, update and close customer accounts; record holidays |
| **Orders** | One or more publications per customer, on chosen days, with start/end dates |
| **Publications & Stock** | Name, type, frequency, price and quantity in stock; low-stock warning |
| **Deliveries** | Daily delivery docket per delivery area (24 areas); mark each delivery ✓ delivered / ✗ missed |
| **Billing** | Monthly bill per customer listing each delivery by date and price, with a total; record payments |

Full requirements: `News Agent Delivery System.docx` in this repository (user stories with acceptance criteria).

---

## Team

| Name | GitHub | Role |
|---|---|---|
| _Name_ | @Pab1312 | _Role_ |
| _Name_ | @Bread-crypto | _Role_ |
| _Name_ | @_username_ | _Role_ |
| Rodion Omelich | @_username_ | _Role_ |

---

## Development approach

- **SDLC:** Agile – Scrum, with V-Model test front-loading (tests are designed from the acceptance criteria before coding).
- **Backlog:** user stories with priority (MoSCoW) and story points.
- **Sprints:** progress reviewed weekly at the practical class.

---

## Tech stack

| Area | Tool |
|---|---|
| Language | Java |
| IDE | Eclipse |
| Testing | JUnit 5 |
| Database | MySQL _(to be confirmed)_ |
| Build | Maven _(planned)_ |
| Version control | Git + GitHub |

---

## Getting started

### 1. Clone the project into Eclipse

1. `File → Import… → Git → Projects from Git (with smart import)`
2. `Clone URI` → paste `https://github.com/Bread-crypto/Assignment1.git`
3. Leave User/Password empty (the repo is public) → `Next` → branch `master` → `Finish`

### 2. Before you push for the first time

GitHub does not accept your normal password from Eclipse. Create a **Personal Access Token**:
GitHub → Settings → Developer settings → Personal access tokens → Tokens (classic) → Generate new token → tick `repo`.
When Eclipse asks for a password on push, paste the token.

---

## How we work with Git

**Never commit directly to `master`.** Every change goes through a branch and a pull request.

```
1. Team → Pull                       (get the latest master)
2. Team → Switch To → New Branch…    (e.g. feature/US-001-register-customer)
3. Write code + tests
4. Team → Commit… → Commit and Push
5. On GitHub: Compare & pull request → ask a teammate to review
6. After merge: Team → Switch To → master → Team → Pull
```

**Branch names**

| Type | Example |
|---|---|
| New feature (user story) | `feature/US-019-generate-bill` |
| Bug fix | `fix/bill-total-rounding` |
| Docs / setup | `docs/readme`, `setup/maven` |

**Commit messages** – short and specific, with the story number when there is one:
`US-001: validate customer phone number (10 digits)`

---

## Project status

- [x] Repository created
- [x] User stories (v1)
- [ ] User stories reviewed and finalised
- [ ] Project structure (packages, Maven, .gitignore)
- [ ] Sprint 1 – Customers and Publications
- [ ] Sprint 2 – Orders, Holidays, Stock
- [ ] Sprint 3 – Delivery dockets
- [ ] Sprint 4 – Billing and testing
