package com.ledgerlib.core;

public record Entry(AccountId account, Money amount, EntrySide side) {

}
