package com.ledgerlib.core;

public class InsufficientFundsException extends LedgerException {
    private final AccountId accountId;
    private final Money attempted;
    private final Money available;

    public InsufficientFundsException(AccountId accountId, Money attempted, Money available) {
        super(String.format("Insufficient funds for account: %s. Attempted: %s. Available: %s", accountId, attempted,
                available));
        this.accountId = accountId;
        this.attempted = attempted;
        this.available = available;
    }

    public InsufficientFundsException(AccountId accountId, Money attempted, Money available, Throwable cause) {
        super(String.format("Insufficient funds for account: %s. Attempted: %s. Available: %s", accountId, attempted,
                available), cause);
        this.accountId = accountId;
        this.attempted = attempted;
        this.available = available;
    }

    public AccountId accountId() {
        return new AccountId(this.accountId.id());
    }

    public Money attempted() {
        return new Money(this.attempted.amount(), this.attempted.currency());
    }

    public Money available() {
        return new Money(this.available.amount(), this.available.currency());
    }

}
