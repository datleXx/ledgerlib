package com.ledgerlib.core;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Currency;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LedgerServiceTest {

    private Account userAccount;
    private Account cashAccount;
    private LedgerService service;
    private Currency usd;

    @BeforeEach
    void setUp() {
        usd = Currency.getInstance("USD");
        userAccount = new Account(new AccountId("USER-001"), usd);
        cashAccount = new Account(new AccountId("CASH"), usd);
        service = new LedgerService();
    }

    // === Money Tests ===
    @Test
    void testMoneyPlus_CurrencyMismatch_ThrowsException() {
        Money usdMoney = Money.of("100.00", "USD");
        Money eurMoney = Money.of("100.00", "EUR");

        CurrencyMismatchException exception = assertThrows(
                CurrencyMismatchException.class,
                () -> usdMoney.plus(eurMoney));

        assertEquals(Currency.getInstance("USD"), exception.expected());
        assertEquals(Currency.getInstance("EUR"), exception.actual());
    }

    @Test
    void testMoneyMinus_CurrencyMismatch_ThrowsException() {
        Money usdMoney = Money.of("100.00", "USD");
        Money eurMoney = Money.of("100.00", "EUR");

        CurrencyMismatchException exception = assertThrows(
                CurrencyMismatchException.class,
                () -> usdMoney.minus(eurMoney));

        assertEquals(Currency.getInstance("USD"), exception.expected());
        assertEquals(Currency.getInstance("EUR"), exception.actual());
    }

    // === Transaction Tests ===
    @Test
    void testTransaction_Unbalanced_ThrowsException() {
        AccountId a = new AccountId("A");
        AccountId b = new AccountId("B");
        Entry debit = new Entry(a, Money.of("100.00", "USD"), EntrySide.DEBIT);
        Entry credit = new Entry(b, Money.of("50.00", "USD"), EntrySide.CREDIT);

        UnbalancedTransactionException exception = assertThrows(
                UnbalancedTransactionException.class,
                () -> new Transaction(List.of(debit, credit)));

        assertEquals(2, exception.getEntries().size());
    }

    // === LedgerService Tests ===
    @Test
    void testWithdraw_InsufficientFunds_ThrowsException() {
        Money withdrawAmount = Money.of("200.00", "USD");

        InsufficientFundsException exception = assertThrows(
                InsufficientFundsException.class,
                () -> service.withdraw(userAccount, cashAccount, withdrawAmount));

        assertEquals(userAccount.id(), exception.accountId());
        assertEquals(withdrawAmount, exception.attempted());
        assertEquals(Money.of("100.00", "USD"), exception.available());
    }

    @Test
    void testWithdraw_SufficientFunds_Succeeds() {
        Money withdrawAmount = Money.of("50.00", "USD");
        Account updated = service.withdraw(userAccount, cashAccount, withdrawAmount);
        assertEquals(Money.of("50.00", "USD"), updated.getBalance());
    }

    @Test
    void testWithdraw_NullAccount_ThrowsNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> service.withdraw(null, cashAccount, Money.of("100.00", "USD")));
    }

    @Test
    void testWithdraw_NullCashAccount_ThrowsNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> service.withdraw(userAccount, null, Money.of("100.00", "USD")));
    }

    @Test
    void testWithdraw_NullAmount_ThrowsNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> service.withdraw(userAccount, cashAccount, null));
    }

    @Test
    void testWithdraw_NegativeAmount_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> service.withdraw(userAccount, cashAccount, Money.of("-100.00", "USD")));
    }

    @Test
    void testWithdraw_CurrencyMismatch_ThrowsCurrencyMismatchException() {
        Money eurAmount = Money.of("100.00", "EUR");

        CurrencyMismatchException exception = assertThrows(
                CurrencyMismatchException.class,
                () -> service.withdraw(userAccount, cashAccount, eurAmount));

        assertEquals(usd, exception.expected());
        assertEquals(Currency.getInstance("EUR"), exception.actual());
    }

    @Test
    void testWithdraw_ExactBalance_Succeeds() {
        Money withdrawAmount = Money.of("100.00", "USD");
        Account updated = service.withdraw(userAccount, cashAccount, withdrawAmount);
        assertEquals(Money.of("0.00", "USD"), updated.getBalance());
    }
}
