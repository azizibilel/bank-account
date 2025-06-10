package com.capco.domain.exception;

public sealed class FunctionalException extends RuntimeException permits AccountNotFoundException, InsufficientFundsException {
    protected FunctionalException(String context) {
        super(context);
    }
}
