# Bank Account Kata

This project contains an implementation of the Bank Account Kata using Java with a Craft, Clean Code, TDD (
Test-Driven Development) , DDD (Domain Driven Design) and hexagonal architecture.

## Kata Description

The Bank Account Kata involves creating a banking system that allows users to make deposits, withdrawals, check the
account balance, and view the transaction history.

### Features

The main features of the Bank Account Kata include:

- Depositing a positive amount into an empty or existing account.
- Withdrawing a positive amount from an account with sufficient balance.
- Checking the account balance.

### Test Scenarios

The test scenarios for the Bank Account Kata are as follows:

- Deposit a positive amount into an empty account.
- Deposit a positive amount into an existing account with a positive balance.
- Deposit a negative amount (should throw an exception).
- Deposit an amount equal to zero (should not modify the balance).
- Withdraw a positive amount from an account with sufficient balance.
- Withdraw a negative amount (should throw an exception).
- Withdraw an amount greater than the available balance (should throw an exception).
- Withdraw an amount equal to zero (should not modify the balance).
- Withdraw an amount from an empty account  (should throw exception).
- Check the initial balance of a new account (should be zero).
- Check balance in concurrent operations
- Print the transaction history.
- Attempt to deposit/withdraw a negative amount (should throw an exception).
- Test the account behavior with decimal amounts.

## Prerequisites

To run the Bank Account Kata, you will need the following:

- Java JDK (version 8 or higher)
- Maven (to execute the tests)

## Usage Instructions

1. Clone the project from GitHub:

```console
git clone https://github.com/azizibilel/bank-account.git
cd bank-account
```

2. Run the tests:

```console
mvn clean test
```