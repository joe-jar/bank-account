# Bank Account Service

## Description

---

This project is a **Bank Account Management System** built with **Spring Boot**, allowing bank clients to deposit money, withdraw funds, and view account statements showing transaction details like dates, amounts, and balances. The development follows **Test-Driven Development (TDD)** and applies **SOLID principles** to keep the code structured and easy to maintain.

## Requirements

---

- Deposit and Withdrawal functionalities
- Account statement with date, amount, and balance
- Statement printing functionality

## User Stories

---

### US 1:

- In order to save money
- As a bank client
- I want to make a deposit in my account

### US 2:

- In order to retrieve some or all of my savings
- As a bank client
- I want to make a withdrawal from my account

## Technologies Used

---

- Java 23
- Spring Boot 3.4.2
- Maven
- JUnit 5 & Mockito
- SLF4J

- Java 23
- Spring Boot 3.4.2
- Maven
- JUnit 5 & Mockito

## Installation

---

### Clone the Repository

```sh
    git clone https://github.com/your-repo/bank-account-service.git
    cd bank-account-service
```

### Build the Project

```sh
    mvn clean install
```

### Run the Application

```sh
    mvn spring-boot:run
```



## Running Tests

---

Unit tests are written following **Test-Driven Development (TDD)**, covering all valid and invalid use cases. To run the tests, use the following command:

```sh
    mvn test
```



