package com.capco.domain.ports.out;

import com.capco.domain.model.Account;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepositoryPort {
    Account save(Account account);

    Optional<Account> findById(UUID id);
}
