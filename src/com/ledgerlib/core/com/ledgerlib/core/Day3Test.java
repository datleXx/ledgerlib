package com.ledgerlib.core;

import java.util.Currency;
import java.util.List;

public class Day3Test {
    public static void main(String[] args) {
        AccountId accId = new AccountId("SAV-001");
        Currency usd = Currency.getInstance("USD");
        Account account = new Account(accId, usd);
        System.out.println("Initial balance: " + account.getBalance());

        AccountId other = new AccountId("OTHER");
        Money depositAmount = Money.of("500.00", "USD");
        Entry credit = new Entry(accId, depositAmount, EntrySide.CREDIT);
        Entry debit = new Entry(other, depositAmount, EntrySide.DEBIT);
        Transaction depositTx = new Transaction(List.of(credit, debit));

        Account updated = account.addTransaction(depositTx);
        System.out.println("Balance after deposit: " + updated.getBalance());

        // Currency mismatch test
        try {
            AccountId another = new AccountId("ANOTHER");
            Entry eurEntry = new Entry(another, Money.of("100.00", "EUR"), EntrySide.DEBIT);
            Entry eurCredit = new Entry(accId, Money.of("100.00", "EUR"), EntrySide.CREDIT);
            Transaction badTx = new Transaction(List.of(eurEntry, eurCredit));
            updated.addTransaction(badTx);
        } catch (IllegalArgumentException e) {
            System.out.println("Currency mismatch caught: " + e.getMessage());
        }

        // Try to modify the transactions list
        List<Transaction> txList = updated.transactions();
        try {
            txList.add(depositTx); // should throw
        } catch (UnsupportedOperationException e) {
            System.out.println("Transactions list is unmodifiable (as expected)");
        }
    }
}
