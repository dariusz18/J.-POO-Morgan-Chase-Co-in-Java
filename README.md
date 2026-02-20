# J. POO Morgan Chase & Co. — Java

> A full-featured banking system simulation built in Java, inspired by modern e-banking platforms like Revolut. Implements core OOP principles and multiple design patterns across two development stages.

---

## Table of Contents

- [Overview](#overview)
- [Project Structure](#project-structure)
- [Class Hierarchy](#class-hierarchy)
- [Design Patterns](#design-patterns)
- [Key Components](#key-components)
- [Supported Commands](#supported-commands)
- [Service Plans & Fees](#service-plans--fees)
- [Cashback System](#cashback-system)
- [Design Decisions](#design-decisions)
- [How to Run](#how-to-run)

---

## Overview

J. POO Morgan Chase & Co. is a JSON-driven banking engine that simulates the core operations of a modern bank. Users can open multiple account types, manage cards, perform online payments, wire transfers, withdrawals, and split bills — all while being subject to service plans, commission fees, and merchant cashback strategies.

The project was developed across two stages as part of the **OOP (Object-Oriented Programming)** course at **Politehnica University of Bucharest**, progressively adding complexity: from basic account management in Stage 1 to business accounts, cashback strategies, plan upgrades, and split payment flows in Stage 2.

The engine reads a JSON input file and produces a structured JSON output, processing commands sequentially and maintaining a full transaction history for every user.

---

## Project Structure

```
src/
├── org.poo.banking/              # Core banking logic
│   ├── Account.java              # Base account model (classic / savings / business)
│   ├── AccountFactory.java       # Factory for account creation
│   ├── AddAccount.java           # Command: open a new account
│   ├── AddFunds.java             # Command: deposit funds
│   ├── AddInterest.java          # Command: apply interest to savings account
│   ├── Card.java                 # Base card model
│   ├── CardStatus.java           # Command: check/update card status
│   ├── Cashback.java             # Cashback calculation logic
│   ├── CashbackStrategy.java     # Strategy interface for cashback
│   ├── CashWithdrawal.java       # Command: ATM cash withdrawal
│   ├── Constants.java            # Shared numeric constants
│   ├── Converter.java            # Singleton: currency exchange rate manager
│   ├── CreateCard.java           # Command: create a standard card
│   ├── CreateOneTimeCard.java    # Command: create a one-time use card
│   ├── DeleteAccount.java        # Command: close an account
│   ├── DeleteCard.java           # Command: destroy a card
│   ├── FeeCalculate.java         # Commission calculation per service plan
│   ├── MinimumBalance.java       # Command: set minimum balance threshold
│   ├── NrOfTransactions.java     # Cashback strategy: transaction count
│   ├── OneTimeCard.java          # Extends Card; auto-regenerates after use
│   ├── PayOnline.java            # Command: online card payment
│   ├── PrintUsers.java           # Debug command: list all users
│   ├── Report.java               # Command: generate account/spending reports
│   ├── SavingsAccount.java       # Extends Account; adds interest rate support
│   ├── SendMoney.java            # Command: bank wire transfer
│   ├── SpendingsThreshold.java   # Cashback strategy: cumulative spending
│   ├── SplitPayment.java         # Command: split a payment across accounts
│   ├── UpgradePlan.java          # Command: upgrade service plan
│   ├── User.java                 # User model: accounts, plan, history
│   └── WithdrawSavings.java      # Command: transfer from savings to classic
│
├── org.poo.fileio/               # Input parsing (JSON to objects)
│   ├── CommandInput.java
│   ├── CommerciantInput.java
│   ├── ExchangeInput.java
│   ├── ObjectInput.java
│   └── UserInput.java
│
├── org.poo.transactions/         # Transaction layer
│   ├── PrintTransactions.java    # Debug command: print user transaction history
│   ├── Transaction.java          # Immutable transaction model (Builder pattern)
│   └── TransactionHistory.java   # Per-user ordered transaction log
│
├── org.poo.utils/
│   └── Utils.java                # IBAN and card number generation utilities
│
└── org.poo.main/
    ├── Main.java                 # Entry point: reads input, dispatches commands
    └── Test.java                 # Manual single-test runner
```

---

## Class Hierarchy

```
User
 ├── List<Account>
 │    ├── Account (classic)
 │    ├── SavingsAccount (savings)   <- extends Account
 │    └── Account (business)
 │         └── List<Card>
 │              ├── Card (standard)
 │              └── OneTimeCard      <- extends Card
 └── TransactionHistory
      └── List<Transaction>          <- built via TransactionBuilder
```

---

## Design Patterns

| Pattern | Class(es) | Purpose |
|---|---|---|
| **Builder** | `Transaction`, `Transaction.TransactionBuilder` | Transactions vary significantly by type — some have sender/receiver IBANs, others have commerciant data or card info. The Builder avoids a bloated constructor and makes optional fields explicit. |
| **Singleton** | `Converter` | Currency exchange rates are shared application-wide. A single `Converter` instance is initialized once per test and reused across all commands, preventing redundant state and enabling consistent conversions. |
| **Factory** | `AccountFactory` | Centralizes account creation logic. Instantiates `SavingsAccount`, classic `Account`, or business `Account` based on the `accountType` string, hiding construction details from the caller. |
| **Strategy** | `CashbackStrategy`, `NrOfTransactions`, `SpendingsThreshold` | Each merchant defines its own cashback strategy. The Strategy pattern allows plugging in different algorithms without modifying the payment logic. |

---

## Key Components

**`PayOnline`** — The most complex command. Locates the card and its owner, validates card status (not frozen, not blocked), finds the merchant, converts the payment amount to the account's currency via `Converter`, calculates the service fee via `FeeCalculate`, checks for sufficient funds, applies cashback, updates the balance, and records the transaction. If the card is a one-time card, it is destroyed and immediately regenerated.

**`SendMoney`** — Similar pipeline to `PayOnline`. Resolves the sender and receiver accounts (supporting both user IBANs and merchant IBANs), converts the transferred amount into the receiver's currency, deducts the commission from the sender, and logs a sent/received transaction for both parties.

**`CashWithdrawal`** — Validates the user, card, and frozen/minimum-balance status. All withdrawals are in RON. Applies the appropriate commission based on the user's service plan and records a descriptive transaction entry.

**`WithdrawSavings`** — Enforces a minimum age of 21. Requires the user to have at least one classic account in the target currency. Performs a currency conversion from the savings account's currency and transfers the computed amount, including any applicable fee.

**`FeeCalculate`** — Stateless utility that returns the commission multiplier based on the user's current plan. Standard plans pay 0.2% on all transactions; Silver pays 0.1% only on amounts above 500 RON; Gold and Student pay no fees.

**`SavingsAccount`** — Extends `Account` with an `interestRate` field. Interest is added as a percentage of the current balance on demand via the `addInterest` command.

**`OneTimeCard`** — Extends `Card` with a `one_time` type flag. After any successful payment, the card is destroyed and a new one is auto-generated on the same account.

**`Converter`** — Singleton initialized at the start of each test with the input exchange rate table. Builds a full transitive rate map (e.g., USD to EUR to RON) using a triple-loop traversal, enabling indirect currency conversion without explicit chain definitions.

---

## Supported Commands

### Account & Card Management

| Command | Description |
|---|---|
| `addAccount` | Creates a classic, savings, or business account for a user |
| `addFunds` | Deposits funds into an account |
| `deleteAccount` | Closes an account (only if balance is 0) |
| `createCard` | Creates a standard card linked to an account |
| `createOneTimeCard` | Creates a single-use card; regenerated after each payment |
| `deleteCard` | Destroys a card and records the event |
| `setMinimumBalance` | Sets a floor balance; cards are frozen if balance drops below it |
| `checkCardStatus` | Checks if a card is active, at warning level, or frozen |

### Transactions

| Command | Description |
|---|---|
| `payOnline` | Card payment to a merchant |
| `sendMoney` | Wire transfer between accounts or to a merchant |
| `cashWithdrawal` | ATM cash withdrawal in RON |
| `splitPayment` | Splits a bill equally or by custom amounts; requires all parties to accept |
| `acceptSplitPayment` | Confirms a pending split payment request |
| `rejectSplitPayment` | Cancels the entire split payment for all parties |
| `withdrawSavings` | Moves funds from savings to a classic account |
| `addInterest` | Applies interest to a savings account |
| `changeInterestRate` | Updates the interest rate of a savings account |
| `upgradePlan` | Upgrades the user's service plan (with RON fee) |

### Reports & Debug

| Command | Description |
|---|---|
| `printUsers` | Lists all users, their accounts, and cards |
| `printTransactions` | Outputs the full transaction history of a user |
| `report` | Classic report: account details and transactions in a time range |
| `spendingReport` | Spending breakdown by merchant category |
| `businessReport` | Business account analytics: by transaction or by merchant |
| `setAlias` | Assigns a nickname to an IBAN for easier transfers |

---

## Service Plans & Fees

Each user is assigned a service plan at registration, defaulting to **Student** for students and **Standard** for everyone else.

| Plan | Commission | Upgrade Cost |
|---|---|---|
| **Standard** | 0.2% on all transactions | — |
| **Student** | No fees | — |
| **Silver** | 0.1% on transactions >= 500 RON | 100 RON from Standard/Student |
| **Gold** | No fees | 250 RON from Silver / 350 RON from Standard/Student |

Gold status is also granted automatically after 5 payments of at least 300 RON each on a Silver plan. Downgrades are not permitted.

---

## Cashback System

Cashback is calculated **per account** and depends on the merchant's defined strategy.

### Strategy 1: `nrOfTransactions`

Counts transactions at merchants of type Food, Clothes, or Tech. Upon reaching certain thresholds, a one-time discount is unlocked for the next purchase in the corresponding category:

- 2 transactions at any Food/Clothes/Tech merchant → 2% off the next Food purchase
- 5 transactions → 5% off the next Clothes purchase
- 10 transactions → 10% off the next Tech purchase

Each discount is consumed on first use and does not reset. Discounts can be applied at any merchant belonging to the eligible category, regardless of strategy type.

### Strategy 2: `spendingThreshold`

Tracks cumulative spending (in RON) at merchants of this type. Cashback is applied to the transaction that crosses each threshold:

| Threshold | Standard / Student | Silver | Gold |
|---|---|---|---|
| 100 RON | 0.1% | 0.3% | 0.5% |
| 300 RON | 0.2% | 0.4% | 0.55% |
| 500 RON | 0.25% | 0.5% | 0.7% |

---

## Design Decisions

### One Class Per Command
Every banking operation has its own dedicated class (e.g., `PayOnline`, `SendMoney`, `CashWithdrawal`). This enforces the Single Responsibility Principle and keeps `Main.java` clean — it only dispatches, never processes.

### Static Command Instances in Main
Command handler objects are instantiated once as `private static final` fields in `Main`. Since these classes carry no mutable state, this avoids garbage collection overhead across the potentially hundreds of command invocations per test.

### Immutable Transactions via Builder
`Transaction` objects are fully immutable once built. The `TransactionBuilder` collects only the fields relevant to the specific transaction type before calling `.build()`, keeping the transaction log consistent and thread-safe.

### Singleton Converter with Transitive Rates
`Converter` is initialized once per test by clearing and rebuilding its rate map. The Floyd-Warshall-style loop computes indirect rates (e.g., USD to RON via EUR) automatically, so callers only ever need to call `convert(from, to, amount)` without knowing the conversion path.

### Shallow, Purposeful Inheritance
`SavingsAccount` and `OneTimeCard` extend their base classes only to add the minimal extra behavior they need (interest rate and auto-regeneration respectively). All other account and card types are handled via a `type` string field, avoiding an over-engineered class hierarchy.

---

## How to Run

1. Open the project in **IntelliJ IDEA** from the directory containing `pom.xml`.
2. If the project does not load correctly, delete `.idea/` and `target/` and re-open via **File -> Open**.
3. To reinstall dependencies, open the Maven panel, go to **lifecycle**, and double-click **install**.
4. Run `Main.java` to process all test files from `input/` and write results to `output/`.
5. Use `Test.java` to run a single named test file interactively via console input.
6. The checker is invoked automatically at the end of `Main.main()` via `Checker.calculateScore()`.
