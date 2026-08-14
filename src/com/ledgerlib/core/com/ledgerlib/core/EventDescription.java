package com.ledgerlib.core;

public final class EventDescription {
    public static String describe(LedgerEvent event) {
        return switch (event) {
            case Deposit d -> "Deposit of " + d.amount() + " to " + d.account();
            case Withdrawal w -> "Withdrawal of " + w.amount() + " from " + w.account();
            case Transfer t -> "Transfer of " + t.amount() + " from " + t.from() + " to " + t.to();
        };
    }
}
