package com.ledgerlib.core;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class LedgerService {
  public static Map<AccountId, BigDecimal> getBalances(List<Transaction> transactions) {
    return transactions.stream()
        .flatMap(tx -> tx.entries().stream())
        .collect(
            Collectors.groupingBy(
                Entry::account,
                Collectors.reducing(
                    BigDecimal.ZERO,
                    entry -> {
                      BigDecimal amount = entry.amount().amount();
                      return entry.side() == EntrySide.CREDIT ? amount : amount.negate();
                    },
                    BigDecimal::add)));
  }

  public static List<Map.Entry<AccountId, BigDecimal>> getTopNAccounts(
      List<Transaction> transactions, int n) {
    Map<AccountId, BigDecimal> turnOverMap =
        transactions.stream()
            .flatMap(tx -> tx.entries().stream())
            .collect(
                Collectors.groupingBy(
                    Entry::account,
                    Collectors.reducing(
                        BigDecimal.ZERO, entry -> entry.amount().amount().abs(), BigDecimal::add)));

    return turnOverMap.entrySet().stream()
        .sorted(Map.Entry.<AccountId, BigDecimal>comparingByValue().reversed())
        .limit(n)
        .collect(Collectors.toList());
  }

  public static Set<Currency> getDistinctCurrency(List<Transaction> transactions) {
    return transactions.stream()
        .flatMap(tx -> tx.entries().stream())
        .map(entry -> entry.amount().currency())
        .collect(Collectors.toSet());
  }

  public static List<Transaction> filterTransactionByThreshold(
      List<Transaction> transactions, BigDecimal threshold) {
    return transactions.stream()
        .filter(
            tx -> {
              BigDecimal total =
                  tx.entries().stream()
                      .map(entry -> entry.amount().amount().abs())
                      .reduce(BigDecimal.ZERO, BigDecimal::add);
              return total.compareTo(threshold) > 0;
            })
        .sorted(Comparator.comparing(Transaction::timestamp))
        .collect(Collectors.toList());
  }

  public static void main(String[] args) {
    // Create accounts
    AccountId accA = new AccountId("A");
    AccountId accB = new AccountId("B");
    AccountId accC = new AccountId("C");

    // Create entries
    Entry entry1 = new Entry(accA, Money.of("100.00", "USD"), EntrySide.DEBIT);
    Entry entry2 = new Entry(accB, Money.of("100.00", "USD"), EntrySide.CREDIT);
    Entry entry3 = new Entry(accB, Money.of("50.00", "USD"), EntrySide.DEBIT);
    Entry entry4 = new Entry(accC, Money.of("50.00", "USD"), EntrySide.CREDIT);
    Entry entry5 = new Entry(accA, Money.of("30.00", "USD"), EntrySide.CREDIT);
    Entry entry6 = new Entry(accC, Money.of("30.00", "USD"), EntrySide.DEBIT);

    // Create transactions
    Transaction tx1 = new Transaction(List.of(entry1, entry2));
    Transaction tx2 = new Transaction(List.of(entry3, entry4));
    Transaction tx3 = new Transaction(List.of(entry5, entry6));

    // Get balances
    List<Transaction> transactions = List.of(tx1, tx2, tx3);
    Map<AccountId, BigDecimal> balances = getBalances(transactions);

    balances.forEach((account, balance) -> System.out.println(account + ": $" + balance));
  }

  public Account withdraw(Account account, Account cashAccount, Money amount) {
    // 1. Validate nulls
    if (account == null) throw new NullPointerException("Account cannot be null");
    if (cashAccount == null) throw new NullPointerException("Cash account cannot be null");
    if (amount == null) throw new NullPointerException("Amount cannot be null");

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
