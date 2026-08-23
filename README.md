# ATM Management System

A Java-based console ATM simulation that replicates core banking operations using Object-Oriented Programming and Java Collections. Built to practice secure authentication logic, input validation, and multi-class OOP design.

## 🚀 Features
- Create new bank accounts with unique 11-digit account number validation
- 6-digit PIN creation and verification
- Multiple account support using `HashMap`
- PIN-based authentication with 3-attempt lockout for security
- Deposit and withdraw funds with real-time validation (min withdrawal, sufficient balance checks)
- Check account balance
- Transaction history log with timestamps (`LocalDateTime`)
- Robust exception handling for invalid inputs (`InputMismatchException`, regex validation)

## 🛠️ Technologies Used
- Java
- Object-Oriented Programming (Account/Main class separation)
- Java Collections Framework — `HashMap`, `ArrayList`
- Exception Handling
- `LocalDateTime` for timestamping

## 🏗️ Project Structure
```text
ATM-Management-System
│
├── Main.java       # Contains Account and Main classes
└── README.md
```

## ▶️ How to Run
```bash
# Clone the repository
git clone https://github.com/Raki0809/atm-simulation-java.git
cd atm-simulation-java

# Compile
javac Main.java

# Run
java Main
```

## 📋 Sample Flow
1. Choose "Create Account" → enter 11-digit account number, 6-digit PIN, initial balance (≥ ₹2000)
2. Choose "Existing Account" → log in with account number and PIN
3. Access ATM menu → Deposit / Withdraw / Check Balance / View Transaction History

## 🔒 Security Notes
- PINs are validated for exact 6-digit format before matching
- Account locks after 3 consecutive incorrect PIN attempts
- All transactions require successful PIN re-verification

## 📈 Future Improvements
- Persist account data using file storage or a database (currently in-memory only)
- Use `BigDecimal` instead of `double` for currency precision
- Separate UI logic from business logic (decouple `Scanner` from `Account` class)
- Add unit tests for core banking operations

