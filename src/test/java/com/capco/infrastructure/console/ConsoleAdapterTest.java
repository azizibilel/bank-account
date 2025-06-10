package com.capco.infrastructure.console;

import com.capco.application.AccountService;
import com.capco.domain.model.Account;
import com.capco.domain.model.Client;
import com.capco.domain.ports.in.AccountServicePort;
import com.capco.domain.ports.out.AccountRepositoryPort;
import com.capco.domain.ports.out.ConsolePort;
import com.capco.infrastructure.persistence.InMemoryAccountRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConsoleAdapterTest {

    private static final UUID ACCOUNT_ID = UUID.fromString("e55e2467-4618-4a0a-9e3a-ebc8917f5e1f");
    private static final UUID CLIENT_ID = UUID.fromString("e0371910-1d68-476b-bbf5-7af15b35ca80");
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream printStream = System.out;
    private AccountServicePort accountService;

    private ConsolePort consolePort;

    @BeforeEach
    void setUp() {
        //Given
        System.setOut(new PrintStream(outputStream));
        Account account = new Account(ACCOUNT_ID, "DAV", new Client(CLIENT_ID, "test"));
        AccountRepositoryPort accountRepositoryPort = new InMemoryAccountRepository();
        accountRepositoryPort.save(account);
        accountService = new AccountService(accountRepositoryPort);
        consolePort = new ConsoleAdapter(accountService);
    }

    @AfterEach
    void tearDown() {
        System.setOut(printStream);
    }

    @Test
    void viewTransaction_should_display_no_transactions_message_when_no_transaction_exist() {
        //When
        consolePort.viewTransactions(ACCOUNT_ID);

        //Then
        String output = outputStream.toString();
        assertTrue(output.contains("No transactions found"));
    }

    @Test
    void viewTransaction_should_display_proper_table_format_when_transactions_exist() {
        //Given
        accountService.deposit(ACCOUNT_ID, new BigDecimal("1500"));
        accountService.withdraw(ACCOUNT_ID, new BigDecimal("300"));
        //When
        consolePort.viewTransactions(ACCOUNT_ID);

        //Then
        String output = outputStream.toString();
        assertAll(
                () -> assertTrue(output.contains("Date")),
                () -> assertTrue(output.contains("Type")),
                () -> assertTrue(output.contains("Amount")),
                () -> assertTrue(output.contains("Balance")),
                () -> assertTrue(output.contains("DEPOSIT")),
                () -> assertTrue(output.contains("1,500.00")),
                () -> assertTrue(output.contains("WITHDRAW")),
                () -> assertTrue(output.contains("300.00")),
                () -> assertTrue(output.contains("1,200.00")),
                () -> assertTrue(output.contains("---------------------+-----------------+"))
        );
    }

}