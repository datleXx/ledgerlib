package com.ledgerlib.core;

public record Deposit(AccountId account, Money amount) implements LedgerEvent {

}
