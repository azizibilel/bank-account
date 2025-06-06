package com.capco.infrastructure.console;

import com.capco.domain.ports.in.AccountServicePort;
import com.capco.domain.ports.out.ConsolePort;

import java.util.UUID;

public class ConsoleAdapter implements ConsolePort {
    private final AccountServicePort accountServicePort;

    public ConsoleAdapter(AccountServicePort accountServicePort) {
        this.accountServicePort = accountServicePort;
    }

    public void viewTransactions(UUID accountId) {
        accountServicePort.viewTransactions(accountId);
    }
}
