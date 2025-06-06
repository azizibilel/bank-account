package com.capco.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Transaction(TransactionType transactionType, LocalDateTime date, BigDecimal amount, BigDecimal balance) {
}
