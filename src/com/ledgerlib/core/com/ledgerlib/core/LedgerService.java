package com.ledgerlib.core;

import java.math.BigDecimal;
import java.util.List;

public class LedgerService {
    public Account withdraw(Account account, Account cashAccount, Money amount) {
        // 1. Validate nulls
        if (account == null)
            throw new NullPointerException("Account cannot be null");
        if (cashAccount == null)
            throw new NullPointerException("Cash account cannot be null");
        if (amount == null)
            throw new NullPointerException("Amount cannot be null");

        // 2. Validate amount is positive
        if (amount.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        // 3. Validate currency matches account
        if (!amount.currency().equals(account.baseCurrency())) {
            throw new CurrencyMismatchException(account.baseCurrency(), amount.currency());
        }

        // 4. Check sufficient balance
        if (account.getBalance().amount().compareTo(amount.amount()) < 0) {
            throw new InsufficientFundsException(account.id(), amount, account.getBalance());
        }

        // 5. Create withdrawal transaction
        Entry debitEntry = new Entry(account.id(), amount, EntrySide.CREDIT); // Decrease account
        Entry creditEntry = new Entry(cashAccount.id(), amount, EntrySide.DEBIT); // Increase cash

        Transaction withdrawalTx = new Transaction(List.of(debitEntry, creditEntry));

        // 6. Return updated account
        return account.addTransaction(withdrawalTx);
    }

}
