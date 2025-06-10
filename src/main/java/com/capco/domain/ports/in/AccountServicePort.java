package com.capco.domain.ports.in;

import com.capco.domain.model.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface AccountServicePort {


    BigDecimal getBalance(UUID accountId);

    List<Transaction> getTransactions(UUID accountId);

    void deposit(UUID accountId, BigDecimal amount);

    void withdraw(UUID accountId, BigDecimal amount);


}
