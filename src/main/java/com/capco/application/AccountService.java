package com.capco.application;

import com.capco.domain.exception.AccountNotFoundException;
import com.capco.domain.model.Account;
import com.capco.domain.model.Transaction;
import com.capco.domain.ports.in.AccountServicePort;
import com.capco.domain.ports.out.AccountRepositoryPort;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.capco.domain.model.constant.Message.ACCOUNT_NOT_FOUND;

public class AccountService implements AccountServicePort {

    private final AccountRepositoryPort accountRepositoryPort;


    public AccountService(AccountRepositoryPort accountRepositoryPort) {
        this.accountRepositoryPort = accountRepositoryPort;
    }

    @Override
    public BigDecimal getBalance(UUID accountId) {
        return this.getAccount(accountId).getBalance();
    }

    @Override
    public List<Transaction> getTransactions(UUID accountId) {
        return this.getAccount(accountId).getTransactions();
    }


    @Override
    public void deposit(UUID accountId, BigDecimal amount) {
        Account account = this.getAccount(accountId);
        account.deposit(amount);
        accountRepositoryPort.save(account);
    }

    @Override
    public void withdraw(UUID accountId, BigDecimal amount) {
        Account account = this.getAccount(accountId);
        account.withdraw(amount);
        accountRepositoryPort.save(account);
    }

    private Account getAccount(UUID id) {
        return accountRepositoryPort.findById(id).orElseThrow(() -> new AccountNotFoundException(ACCOUNT_NOT_FOUND + id.toString()));
    }
}
