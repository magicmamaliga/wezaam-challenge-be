# Withdrawal Processing – Implementation & Testing Solution

## 1. Project Overview
Implemented a withdrawal processing system with:
- REST API for scheduling and retrieving withdrawals
- Business logic and validations
- Scheduled background processing
- DTO factories
- Comprehensive unit and integration tests
- Validation & exception handling

---

## 2. Entities

- Cleaned Up entities, used lombok
- retired Withdrawal entity

---

### 3. DTOs and DTO factories

- added DTOs whenever was needed, 
- added DTO factories for cleaner business logic
- added @Valid validations for the rest requests for auto validation

---

### 4. SQL Test Data

- right now the SQL generates whenever Spring starts up, it is only for this small project. import.sql would be used in tests, to set up simple test environment.

---

### 5. Exception Handling

- created global exception handler to help with rest exceptions

---

### 8. WithdrawalService

- as it was said that "withdrawal can be executed as soon as possible (note: it doesn't mean immediately)",  the process withdrawal on demand was removed, and the next run of the scheduled service will execute it
- refactored WithdrawalService, noticed the similarities in the Withdrawal and ScheduledWithdrawal, so decided to retire "withdrawals" and keep ScheduledWithdrawal
- moved withdrawal processing to separate component 

---

### 9. Upgrade Maven dependencies

- upgraded libraries to be up to date and decreased vulnerabilities

### 10. OPTIONAL Retry events

- Created a new table for failed events, added a Retry and a save to db in case retries fail, so it persists