package com.ledgerlib.core;

public record Withdrawal(AccountId account, Money amount) implements LedgerEvent {

}
