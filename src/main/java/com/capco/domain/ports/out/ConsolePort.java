package com.capco.domain.ports.out;

import java.util.UUID;

public interface ConsolePort {

    void viewTransactions(UUID accountId);
}
