package com.ledgerlib.core;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayList<T> implements Iterable<T> {

    private static final int DEFAULT_CAPACITY = 10;

    private Object[] elements;
    private int size;
    private int modCount;

    public MyArrayList() {
        this(DEFAULT_CAPACITY);
    }

    public MyArrayList(int capacity) {
        if (capacity < 0) throw new IllegalArgumentException("Initial capacity cannot be negative");
        this.elements = new Object[capacity];
        this.size = 0;
        this.modCount = 0;
    }

    private void grow() {
        int oldCapacity = elements.length;
        int newCapacity = oldCapacity == 0
                ? DEFAULT_CAPACITY
                : oldCapacity * 2;
        elements = Arrays.copyOf(elements, newCapacity);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void add(T element) {
        if (size >= elements.length) grow();
        elements[size] = element;
        size++;
        modCount++;
    }

    public T get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        return (T) elements[index];
    }

    public T remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);

        T removed = (T) elements[index];
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i+1];
        }
        elements[size - 1] = null;
        size--;
        modCount++;

        return removed;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        modCount++;
        size = 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int expectedModCount = modCount;
            private int cursor = 0;
            private int lastReturned = -1;
            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            public T next() {
                checkForCommodification();
                if (!hasNext()) throw new NoSuchElementException();
                lastReturned = cursor;
                cursor++;
                return (T) elements[lastReturned];
            }

            @Override
            public void remove() {
                if (lastReturned < 0) throw new IllegalStateException("No element to remove");
                checkForCommodification();
                MyArrayList.this.remove(lastReturned);
                cursor = lastReturned;
                lastReturned = -1;
                expectedModCount = modCount;
            }

            private void checkForCommodification() {
                if (expectedModCount != modCount) throw new ConcurrentModificationException();
            }
        };
    }

}
