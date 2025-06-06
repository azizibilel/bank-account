package com.capco.infrastructure.persistence;

import com.capco.domain.model.Account;
import com.capco.domain.ports.out.AccountRepositoryPort;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryAccountRepository implements AccountRepositoryPort {
    private final Map<UUID, Account> accounts = new HashMap<>();

    @Override
    public Account save(Account account) {
        return accounts.put(account.getId(), account);
    }

    @Override
    public Optional<Account> findById(UUID id) {
        return Optional.ofNullable(accounts.get(id));
    }
}
