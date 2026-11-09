#  ATM Management System

A Java-based ATM Management System that simulates core banking operations using Object-Oriented Programming, MySQL, and JDBC. The application provides secure PIN authentication, account management, and reliable financial transactions with database persistence.

##  Features

### Account Management
- Create bank accounts with unique 11-digit account numbers.
- Set and verify 6-digit PINs.
- Store and retrieve account details using MySQL.
- Maintain account balances with `BigDecimal` for accurate financial calculations.

### Authentication & Security
- Secure PIN storage using **BCrypt hashing** with automatically generated salts.
- Verify customer PINs using BCrypt password verification.
- Automatically lock accounts after 3 consecutive incorrect PIN attempts.
- Implement a 24-hour account lockout mechanism.
- Support account locking and unlocking.

### Banking Operations
- Deposit money with input validation.
- Withdraw money with minimum amount and sufficient balance checks.
- Check account balance.
- Maintain transaction history with timestamps.
- Store transaction records in MySQL.

### Database & Transaction Management
- MySQL database integration using JDBC.
- PreparedStatements for database operations.
- Foreign key constraints to maintain relational integrity.
- Database transactions using `COMMIT` and `ROLLBACK`.
- ACID transaction principles applied to deposit and withdrawal operations.

##  Technologies Used

| Technology | Purpose |
|---|---|
| Java | Application development |
| MySQL 8.0 | Database management |
| JDBC | Java-MySQL connectivity |
| BCrypt (jBCrypt) | Secure PIN hashing |
| BigDecimal | Accurate financial calculations |
| LocalDateTime | Date and time handling |
| PreparedStatement | Secure SQL operations |
| Git & GitHub | Version control |

##  Project Structure

```text
ATM-Management-System/
│
├── config/
│   └── DatabaseConnection.java
│
├── model/
│   └── Account.java
│
├── repository/
│   └── AccountRepository.java
│
├── lib/
│   ├── mysql-connector-j-26.7.0.jar
│   └── jbcrypt-0.4.jar
│
├── Main.java
├── .gitignore
└── README.md
```

## 🗄️ Database Design

**Database:** `atm_db`

The application uses two main tables:

### 1. Accounts
Stores customer account information.

- AccountNumber
- PinCode (BCrypt hashed)
- AccountBalance
- isLocked
- lockedUntil

### 2. Transactions
Stores financial transaction records.

- TransactionID
- AccountNumber
- TransactionType
- Amount
- TransactionTime

A foreign key connects the Transactions table to the Accounts table.

##  Setup & Installation

### Prerequisites
- Java Development Kit (JDK)
- MySQL Server 8.0 or later
- MySQL Workbench (optional)
- Git

### 1. Clone the Repository

```bash
git clone https://github.com/Raki0809/atm-simulation-java.git
cd atm-simulation-java
```

### 2. Configure MySQL

Create the database:

```sql
CREATE DATABASE atm_db;
```

Create the required `Accounts` and `Transactions` tables using the project's database schema.

### 3. Configure Database Password

The application reads the MySQL password from an environment variable instead of hardcoding it in the source code.

In Windows PowerShell:

```powershell
$env:ATM_DB_PASSWORD="your_mysql_password"
```

Replace the placeholder with your MySQL password.

### 4. Compile the Project

```powershell
javac -cp ".;lib\mysql-connector-j-26.7.0.jar;lib\jbcrypt-0.4.jar" model\Account.java config\DatabaseConnection.java repository\AccountRepository.java Main.java
```

### 5. Run the Application

```powershell
java -cp ".;lib\mysql-connector-j-26.7.0.jar;lib\jbcrypt-0.4.jar" Main
```

## Security Implementation

- Customer PINs are never stored as plaintext.
- BCrypt hashing protects stored PIN credentials.
- PreparedStatements help prevent SQL injection.
- Account lockout protects against repeated incorrect PIN attempts.
- Database transactions help prevent incomplete deposit and withdrawal operations.
- Database credentials are managed through environment variables.

##  Key Concepts Implemented

- Object-Oriented Programming (OOP)
- Model and Repository separation
- JDBC database connectivity
- SQL CRUD operations
- Secure authentication and password hashing
- Financial precision using BigDecimal
- Exception handling and input validation
- ACID properties and transaction management
- Relational database design

##  Future Improvements

- Develop a dedicated admin dashboard.
- Add admin authentication and role-based access.
- Improve application logging and error monitoring.


---

**Developed as a Java and MySQL project to demonstrate secure authentication, database management, and reliable banking transaction processing.**