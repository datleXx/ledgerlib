package com.ledgerlib.core;

public record Transfer(AccountId from, AccountId to, Money amount) implements LedgerEvent {

}
