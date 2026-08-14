package com.ledgerlib.core;

public final class AccountId {
    private final String id;

    public AccountId(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Account ID cannot be null or blank");
        }
        this.id = id;
    }

    public String id() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof AccountId))
            return false;
        AccountId that = (AccountId) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode(); // consistent with equals
    }

    @Override
    public String toString() {
        return "AccountId[" + id + "]";
    }
}
