package com.ledgerlib.core;

import java.util.Currency;

public class CurrencyMismatchException extends LedgerException {
    private final Currency expected;
    private final Currency actual;

    public CurrencyMismatchException(Currency expected, Currency actual) {
        super(String.format("Currency mismatch: expected %s, got %s", expected, actual));
        this.expected = expected;
        this.actual = actual;
    }

    public CurrencyMismatchException(Currency expected, Currency actual, Throwable cause) {
        super(String.format("Currency mismatch: expected %s, got %s", expected, actual), cause);
        this.expected = expected;
        this.actual = actual;
    }

    public Currency expected() {
        return expected;
    }

    public Currency actual() {
        return actual;
    }
}
