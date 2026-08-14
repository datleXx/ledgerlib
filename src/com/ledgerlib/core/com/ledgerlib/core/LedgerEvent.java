package com.ledgerlib.core;

public sealed interface LedgerEvent permits Deposit, Withdrawal, Transfer {

}
