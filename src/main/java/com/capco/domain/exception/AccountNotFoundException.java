package com.capco.domain.exception;

public final class AccountNotFoundException extends FunctionalException {
    public AccountNotFoundException(String context) {
        super(context);
    }
}
