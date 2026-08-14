package com.ledgerlib.core;

import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;

public record Transaction(List<Entry> entries) {
    public Transaction {
        entries = List.copyOf(entries);
        if (entries.isEmpty())
            throw new IllegalArgumentException("Empty arg");

        Entry firstEntry = entries.get(0);
        Currency currency = firstEntry.amount().currency();

        Money sumCredit = Money.of("0", currency.getCurrencyCode());
        Money sumDebit = Money.of("0", currency.getCurrencyCode());

        for (Entry e : entries) {
            if (!e.amount().currency().equals(currency))
                throw new CurrencyMismatchException(e.amount().currency(), currency);

            switch (e.side()) {
                case CREDIT -> sumCredit = sumCredit.plus(e.amount());
                case DEBIT -> sumDebit = sumDebit.plus(e.amount());
            }
        }
        if (!sumDebit.equals(sumCredit))
            throw new UnbalancedTransactionException(entries);
    }

    public LocalDateTime timestamp() {
        return LocalDateTime.now();
    }
}
