package com.ledgerlib.core;

import java.util.function.Consumer;
import java.util.function.Function;

public record Err<T, E extends Exception>(E error) implements Result<T, E> {

    @Override
    public boolean isOk() {
        return false;
    }

    @Override
    public boolean isErr() {
        return true;
    }

    @Override
    public T orElse(T defaultValue) {
        return defaultValue;
    }

    @Override
    public T orElseThrow() throws E {
        throw error;
    }

    @Override
    public <U> Result<U, E> map(Function<? super T, ? extends U> mapper) {
        return new Err<>(error);
    }

    @Override
    public <U> Result<U, E> flatMap(Function<? super T, Result<U, E>> mapper) {
        return new Err<>(error);
    }

    @Override
    public Result<T, E> peek(Consumer<? super T> consumer) {
        return this; // no value to consume
    }
}