# 🏦 J. POO Morgan Chase & Co. — Java

A banking system simulation built with OOP principles and multiple design patterns.

## 🏗️ Class Structure
```
User
 └── Account (classic / savings / business)
      └── Card
           └── OneTimeCard
User
 └── TransactionHistory
      └── Transaction (Builder)
```

## 🎨 Design Patterns

| Pattern | Where | Why |
|---|---|---|
| **Builder** | `Transaction` | Flexible construction of varied transaction types with optional fields |
| **Singleton** | `Converter` | Single global instance managing currency exchange rates |
| **Factory** | `AccountFactory` | Encapsulates creation of classic, savings, and business accounts |
| **Strategy** | `CashbackStrategy` | Pluggable cashback logic — by transaction count or spending threshold |

## 🔧 Key Components

- **`PayOnline`** — finds merchant → converts amount via `Converter` → calculates commission → validates funds → records transaction
- **`SendMoney`** — same pipeline as `PayOnline` with sender/receiver account resolution
- **`CashWithdrawal`** — validates card/user state, calculates commission, updates balance
- **`WithdrawalSavings`** — age check, classic account requirement, currency conversion, balance update
- **`FeeCalculate`** — returns commission rate based on user plan
- **`SavingsAccount`** — extends `Account` with interest rate support
- **`OneTimeCard`** — extends `Card`; auto-freezes after a single use

## 💡 Design Decisions

- Each command has its **own class** for separation of concerns
- **Static variables** prevent repeated instantiation of utility objects
- Inheritance kept shallow and purposeful (`SavingsAccount`, `OneTimeCard`)
