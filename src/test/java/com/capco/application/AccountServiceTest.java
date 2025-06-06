package com.capco.application;

import com.capco.domain.exception.InsufficientFundsException;
import com.capco.domain.model.Account;
import com.capco.domain.model.Client;
import com.capco.domain.model.Transaction;
import com.capco.domain.model.TransactionType;
import com.capco.domain.ports.in.AccountServicePort;
import com.capco.domain.ports.out.AccountRepositoryPort;
import com.capco.infrastructure.persistence.InMemoryAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.capco.domain.model.constant.Message.EMPTY_ACCOUNT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DisplayName("Bank account test case")
class AccountServiceTest {
    private static final UUID ACCOUNT_ID = UUID.fromString("e55e2467-4618-4a0a-9e3a-ebc8917f5e1f");
    private static final UUID CLIENT_ID = UUID.fromString("e0371910-1d68-476b-bbf5-7af15b35ca80");
    private AccountServicePort accountService;

    @BeforeEach
    void setUp() {
        //Given
        Account account = new Account(ACCOUNT_ID, "DAV", new Client(CLIENT_ID, "test"));
        AccountRepositoryPort accountRepositoryPort = new InMemoryAccountRepository();
        accountRepositoryPort.save(account);
        accountService = new AccountService(accountRepositoryPort);
    }

    @Test
    @DisplayName("New account should have zero balance")
    void test_create_new_account_should_have_zero_balance() {
        assertEquals(BigDecimal.ZERO, accountService.getBalance(ACCOUNT_ID));
    }

    @Test
    @DisplayName("Deposit positive amount on empty account")
    void test_deposit_positive_amount_on_empty_account_should_update_balance_and_transaction_histories() {

        //Given
        Transaction expectedTransaction = new Transaction(TransactionType.DEPOSIT, LocalDateTime.now(), new BigDecimal("100"), new BigDecimal("100"));
        //When
        accountService.deposit(ACCOUNT_ID, new BigDecimal("100"));

        //Then
        assertEquals(new BigDecimal("100"), accountService.getBalance(ACCOUNT_ID));
        assertEquals(1, accountService.getTransactions(ACCOUNT_ID).size());


        Transaction addedTransaction = accountService.getTransactions(ACCOUNT_ID).get(0);

        assertEquals(TransactionType.DEPOSIT, addedTransaction.transactionType());
        assertEquals(expectedTransaction.amount(), addedTransaction.amount());
        assertEquals(expectedTransaction.balance(), addedTransaction.balance());
    }

    @Test
    @DisplayName("Deposit positive amount on account with positive balance")
    void test_deposit_positive_amount_on_account_with_positive_balance_should_update_balance_and_transaction_histories() {
        //When
        accountService.deposit(ACCOUNT_ID, new BigDecimal("500"));
        accountService.deposit(ACCOUNT_ID, new BigDecimal("300"));

        //Then
        assertEquals(new BigDecimal("800"), accountService.getBalance(ACCOUNT_ID));
        assertEquals(2, accountService.getTransactions(ACCOUNT_ID).size());

        Transaction expectedTransaction1 = new Transaction(TransactionType.DEPOSIT, LocalDateTime.now(), new BigDecimal("500"), new BigDecimal("500"));
        Transaction addedTransaction1 = accountService.getTransactions(ACCOUNT_ID).get(0);
        assertEquals(TransactionType.DEPOSIT, addedTransaction1.transactionType());
        assertEquals(expectedTransaction1.amount(), addedTransaction1.amount());
        assertEquals(expectedTransaction1.balance(), addedTransaction1.balance());

        Transaction expectedTransaction2 = new Transaction(TransactionType.DEPOSIT, LocalDateTime.now(), new BigDecimal("300"), new BigDecimal("800"));
        Transaction addedTransaction2 = accountService.getTransactions(ACCOUNT_ID).get(1);
        assertEquals(TransactionType.DEPOSIT, addedTransaction2.transactionType());
        assertEquals(expectedTransaction2.amount(), addedTransaction2.amount());
        assertEquals(expectedTransaction2.balance(), addedTransaction2.balance());
    }

    @Test
    @DisplayName("Deposit negative amount")
    void test_deposit_negative_amount_should_throw_exception_and_not_update_balance_nor_affect_transaction_histories() {
        assertThrows(InsufficientFundsException.class, () -> accountService.deposit(ACCOUNT_ID, new BigDecimal("-50")));
        assertEquals(new BigDecimal("0"), accountService.getBalance(ACCOUNT_ID));
        assertEquals(0, accountService.getTransactions(ACCOUNT_ID).size());
    }

    @Test
    @DisplayName("Deposit zero amount")
    void test_deposit_zero_amount_should_not_modify_balance_nor_affect_transaction_histories() {
        //When
        accountService.deposit(ACCOUNT_ID, new BigDecimal("0"));

        //Then
        assertEquals(new BigDecimal("0"), accountService.getBalance(ACCOUNT_ID));
        assertEquals(0, accountService.getTransactions(ACCOUNT_ID).size());
    }

    // Withdraw Scenarios

    @Test
    @DisplayName("Withdraw positive amount with sufficient balance")
    void test_withdraw_positive_amount_with_sufficient_balance_should_update_balance_and_transaction_histories() {
        //Given
        Transaction expectedTransaction1 = new Transaction(TransactionType.DEPOSIT, LocalDateTime.now(), new BigDecimal("1000"), new BigDecimal("1000"));
        Transaction expectedTransaction2 = new Transaction(TransactionType.WITHDRAW, LocalDateTime.now(), new BigDecimal("300"), new BigDecimal("700"));
        //When
        accountService.deposit(ACCOUNT_ID, new BigDecimal("1000"));
        accountService.withdraw(ACCOUNT_ID, new BigDecimal("300"));

        //Then
        assertEquals(new BigDecimal("700"), accountService.getBalance(ACCOUNT_ID));
        assertEquals(2, accountService.getTransactions(ACCOUNT_ID).size());

        Transaction addedTransaction1 = accountService.getTransactions(ACCOUNT_ID).get(0);
        assertEquals(TransactionType.DEPOSIT, addedTransaction1.transactionType());
        assertEquals(expectedTransaction1.amount(), addedTransaction1.amount());
        assertEquals(expectedTransaction1.balance(), addedTransaction1.balance());

        Transaction addedTransaction2 = accountService.getTransactions(ACCOUNT_ID).get(1);
        assertEquals(TransactionType.WITHDRAW, addedTransaction2.transactionType());
        assertEquals(expectedTransaction2.amount(), addedTransaction2.amount());
        assertEquals(expectedTransaction2.balance(), addedTransaction2.balance());
    }

    @Test
    @DisplayName("Withdraw negative amount")
    void test_withdraw_negative_amount_should_throw_exception_and_not_update_balance_nor_affect_transaction_histories() {
        //When
        accountService.deposit(ACCOUNT_ID, new BigDecimal("500"));

        //Then
        assertThrows(InsufficientFundsException.class, () -> accountService.withdraw(ACCOUNT_ID, new BigDecimal("-50")));
        assertEquals(new BigDecimal("500"), accountService.getBalance(ACCOUNT_ID));
        assertEquals(1, accountService.getTransactions(ACCOUNT_ID).size());
    }

    @Test
    @DisplayName("Withdraw with insufficient funds")
    void test_withdraw_amount_greater_than_balance_should_throw_exception_and_not_update_balance_nor_affect_transaction_histories() {
        //When
        accountService.deposit(ACCOUNT_ID, new BigDecimal("200"));

        //Then
        assertThrows(InsufficientFundsException.class, () -> accountService.withdraw(ACCOUNT_ID, new BigDecimal("300")));
        assertEquals(new BigDecimal("200"), accountService.getBalance(ACCOUNT_ID));
        assertEquals(1, accountService.getTransactions(ACCOUNT_ID).size());
    }

    @Test
    @DisplayName("Withdraw zero amount")
    void test_withdraw_zero_amount_should_not_modify_anything() {
        //When
        accountService.deposit(ACCOUNT_ID, new BigDecimal("100"));
        accountService.withdraw(ACCOUNT_ID, new BigDecimal("0"));


        //Then
        assertEquals(new BigDecimal("100"), accountService.getBalance(ACCOUNT_ID));
        assertEquals(1, accountService.getTransactions(ACCOUNT_ID).size());
    }

    @Test
    @DisplayName("Withdraw an amount from an empty account")
    void test_withdraw_an_amount_should_throws_empty_account_when_account_is_empty() {
        Exception exception = assertThrows(InsufficientFundsException.class, () ->
                accountService.withdraw(ACCOUNT_ID, new BigDecimal("10"))
        );
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(EMPTY_ACCOUNT));
        assertEquals(new BigDecimal("0"), accountService.getBalance(ACCOUNT_ID));
        assertEquals(0, accountService.getTransactions(ACCOUNT_ID).size());
    }


    @Test
    @DisplayName("Transaction history")
    public void test_transaction_history_should_return_exact_list_of_transaction() {
        //When
        accountService.deposit(ACCOUNT_ID, new BigDecimal("200"));
        accountService.withdraw(ACCOUNT_ID, new BigDecimal("10"));
        accountService.deposit(ACCOUNT_ID, new BigDecimal("30"));
        accountService.withdraw(ACCOUNT_ID, new BigDecimal("15"));

        //Then
        List<Transaction> transactionHistories = accountService.getTransactions(ACCOUNT_ID);
        assertEquals(4, transactionHistories.size());
        assertEquals(BigDecimal.valueOf(205), accountService.getBalance(ACCOUNT_ID));
        assertEquals(TransactionType.DEPOSIT, transactionHistories.get(0).transactionType());
        assertEquals(TransactionType.WITHDRAW, transactionHistories.get(1).transactionType());
        assertEquals(TransactionType.DEPOSIT, transactionHistories.get(2).transactionType());
        assertEquals(TransactionType.WITHDRAW, transactionHistories.get(3).transactionType());
    }

}