package com.ledgerlib.core;

public class AccountCache {
  private CustomLRUCache<AccountId, Account> cache;

  public AccountCache(int capacity) {
    if (capacity < 0) throw new IllegalArgumentException("Capacity cannot be negative");
    cache = new CustomLRUCache<>(capacity);
  }

  public Account get(AccountId id) {
    return cache.get(id);
  }

  public void put(AccountId id, Account account) {
    cache.put(id, account);
  }
}
