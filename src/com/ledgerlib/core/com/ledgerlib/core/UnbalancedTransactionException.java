package com.ledgerlib.core;

import java.util.ArrayList;
import java.util.List;

public class UnbalancedTransactionException extends LedgerException {
    private final List<Entry> entries;

    public UnbalancedTransactionException(List<Entry> entries) {
        super(String.format("Transaction unbalanced with %s entries", entries.size()));
        this.entries = List.copyOf(entries);
    }

    public UnbalancedTransactionException(List<Entry> entries, Throwable cause) {
        super(String.format("Transaction unbalanced with %s entries", entries.size()), cause);
        this.entries = List.copyOf(entries);
    }

    public List<Entry> getEntries() {
        return new ArrayList<>(this.entries);
    }
}
