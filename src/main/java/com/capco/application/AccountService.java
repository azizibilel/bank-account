package com.capco.application;

import com.capco.domain.exception.AccountNotFoundException;
import com.capco.domain.model.Account;
import com.capco.domain.model.Transaction;
import com.capco.domain.ports.in.AccountServicePort;
import com.capco.domain.ports.out.AccountRepositoryPort;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static com.capco.domain.model.constant.Message.ACCOUNT_NOT_FOUND;

public class AccountService implements AccountServicePort {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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

    @Override
    public void viewTransactions(UUID accountId) {

        List<Transaction> transactions = this.getTransactions(accountId);
        if (transactions.isEmpty()) {
            System.out.println("\nNo transactions found for this account.");
            return;
        }
        String headerFormat = "%-20s | %-15s | %-15s | %-15s%n";
        String divider = "---------------------+-----------------+-----------------+----------------";
        System.out.println("\nTransaction History:");
        System.out.println(divider);
        System.out.printf(headerFormat, "Date", "Type", "Amount", "Balance");
        System.out.println(divider);

        NumberFormat numberFormat = NumberFormat.getInstance(Locale.FRENCH);
        transactions.forEach(transaction -> {
            System.out.printf(headerFormat,
                    formatter.format(transaction.date()),
                    transaction.transactionType(),
                    this.currencyFormat(transaction.amount()),
                    this.currencyFormat(transaction.balance()));
            System.out.println(divider);
        });
    }

    private String currencyFormat(BigDecimal amount) {
        var dfs = DecimalFormatSymbols.getInstance(Locale.FRENCH);
        dfs.setDecimalSeparator('.');
        dfs.setGroupingSeparator(',');
        return new DecimalFormat("#,###.00", dfs).format(amount);
    }

    private Account getAccount(UUID id) {
        return accountRepositoryPort.findById(id).orElseThrow(() -> new AccountNotFoundException(ACCOUNT_NOT_FOUND + id.toString()));
    }
}
