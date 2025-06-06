package com.capco.domain.model;

import com.capco.domain.exception.InsufficientFundsException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.capco.domain.model.constant.Message.AMOUNT_BT_BALANCE;
import static com.capco.domain.model.constant.Message.EMPTY_ACCOUNT;
import static com.capco.domain.model.constant.Message.NEGATIVE_AMOUNT;

public class Account {

    private final UUID id;
    private final String name;
    private final Client client;
    private final List<Transaction> transactions;
    private BigDecimal balance;

    public Account(UUID id, String name, Client client) {
        this.id = id;
        this.name = name;
        this.balance = BigDecimal.ZERO;
        this.transactions = new ArrayList<>();
        this.client = client;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public List<Transaction> getTransactions() {
        return List.copyOf(this.transactions);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Client getClient() {
        return client;
    }

    public void deposit(BigDecimal amount) {
        this.checkIfValidAmount(amount);
        this.balance = this.getBalance().add(amount);
        this.checkAndAddTransaction(amount, TransactionType.DEPOSIT);
    }

    public void withdraw(BigDecimal amount) {
        this.checkIfValidAmount(amount);
        this.checkIfEnoughMoneyInAccount(amount);
        this.balance = this.getBalance().subtract(amount);
        this.checkAndAddTransaction(amount, TransactionType.WITHDRAW);
    }

    private void checkIfValidAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException(NEGATIVE_AMOUNT);
        }
    }

    private void checkIfEnoughMoneyInAccount(BigDecimal amount) {
        if (this.getBalance().compareTo(BigDecimal.ZERO) == 0) {
            throw new InsufficientFundsException(EMPTY_ACCOUNT);
        }
        if (amount.compareTo(this.getBalance()) > 0) {
            throw new InsufficientFundsException(AMOUNT_BT_BALANCE);
        }
    }

    private void checkAndAddTransaction(BigDecimal amount, TransactionType deposit) {
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            var transaction = new Transaction(deposit, LocalDateTime.now(), amount, this.getBalance());
            this.transactions.add(transaction);
        }
    }


}
