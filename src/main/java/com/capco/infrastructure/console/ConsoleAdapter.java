package com.capco.infrastructure.console;

import com.capco.application.util.CurrencyUtils;
import com.capco.domain.model.Transaction;
import com.capco.domain.ports.in.AccountServicePort;
import com.capco.domain.ports.out.ConsolePort;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class ConsoleAdapter implements ConsolePort {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final AccountServicePort accountServicePort;

    public ConsoleAdapter(AccountServicePort accountServicePort) {
        this.accountServicePort = accountServicePort;
    }

    public void viewTransactions(UUID accountId) {
        List<Transaction> transactions = accountServicePort.getTransactions(accountId);
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

        transactions.forEach(transaction -> {
            System.out.printf(headerFormat,
                    formatter.format(transaction.date()),
                    transaction.transactionType(),
                    CurrencyUtils.currencyFormat(transaction.amount()),
                    CurrencyUtils.currencyFormat(transaction.balance()));
            System.out.println(divider);
        });
    }
}
