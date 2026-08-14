package com.ledgerlib.core;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public final class Account {
    private final AccountId id;
    private final Currency baseCurrency;
    private final List<Transaction> transactions;

    public Account(AccountId id, Currency baseCurrency) {
        this.id = id;
        this.baseCurrency = baseCurrency;
        this.transactions = List.of();
    }

    public Account(AccountId id, Currency baseCurrency, List<Transaction> transactions) {
        this.id = id;
        this.baseCurrency = baseCurrency;

        // Validate all transactions use the base currency
        for (Transaction tx : transactions) {
            for (Entry entry : tx.entries()) {
                if (!entry.amount().currency().equals(baseCurrency)) {
                    throw new IllegalArgumentException(
                            "Transaction contains entry with currency " + entry.amount().currency() +
                                    ", but account currency is " + baseCurrency);
                }
            }
        }

        // Defensive copy
        this.transactions = List.copyOf(transactions);
    }

    public AccountId id() {
        return new AccountId(id.id());
    }

    public Currency baseCurrency() {
        return baseCurrency;
    }

    public List<Transaction> transactions() {
        return List.copyOf(this.transactions);
    }

    public Account addTransaction(Transaction tx) {
        for (Entry entry : tx.entries()) {
            if (!entry.amount().currency().equals(baseCurrency)) {
                throw new IllegalArgumentException(
                        "Transaction contains entry with currency " + entry.amount().currency() +
                                ", but account currency is " + baseCurrency);
            }
        }
        List<Transaction> copy = new ArrayList<>(this.transactions);
        copy.add(tx);

        return new Account(id, baseCurrency, List.copyOf(copy));
    }

    public Money getBalance() {
        Money balance = Money.of("0.00", baseCurrency.getCurrencyCode());
        for (Transaction tx : transactions) {
            for (Entry entry : tx.entries()) {
                if (entry.side() == EntrySide.CREDIT) {
                    balance = balance.plus(entry.amount());
                } else {
                    balance = balance.minus(entry.amount());
                }
            }
        }
        return balance;
    }
}
