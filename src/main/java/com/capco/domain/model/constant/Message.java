package com.capco.domain.model.constant;

public abstract class Message {

    public static final String NEGATIVE_AMOUNT = "Cannot deposit or withdraw negative amount";
    public static final String EMPTY_ACCOUNT = "Cannot withdraw money from an empty account";

    public static final String AMOUNT_BT_BALANCE = "Cannot withdraw more money than is currently in the account";

    public static final String ACCOUNT_NOT_FOUND = "account not found with id:";
}
