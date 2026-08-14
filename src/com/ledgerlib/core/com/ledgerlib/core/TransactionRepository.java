package com.ledgerlib.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TransactionRepository implements Iterable<Transaction> {
    private final MyArrayList<Transaction> transactions = new MyArrayList<>();

    public void save(Transaction tx) {
        if (tx == null) throw new IllegalArgumentException("Transaction cannot be null");
        transactions.add(tx);
    }

    public Transaction get(int index) {
        return transactions.get(index);
    }

    public int size() {
        return transactions.size();
    }

    public boolean isEmpty() {
        return transactions.isEmpty();
    }

    public List<Transaction> findall() {
        List<Transaction> copy = new ArrayList<>();
        for (Transaction tx: transactions) copy.add(tx);
        return copy;
    }

    public void clear() {
        transactions.clear();
    }

    public boolean contains(Transaction tx) {
        if (tx == null) return false;
        for (Transaction t: transactions) {
            if (t.equals(tx)) return true;
        }
        return false;
    }

    @Override
    public Iterator<Transaction> iterator() {
        return transactions.iterator();
    }
}
