package com.ledgerlib.core;

import java.util.function.Consumer;
import java.util.function.Function;

public record Ok<T, E extends Exception>(T value) implements Result<T, E> {
    @Override
    public boolean isOk() { return true; }

    @Override
    public boolean isErr() { return false; }

    @Override
    public T orElse(T defaultValue) {
        return value;
    }

    @Override
    public T orElseThrow() throws E{
        return value;
    }

    @Override
    public <U> Result<U, E> map(Function<? super T, ? extends U> mapper) {
        return new Ok<>(mapper.apply(value));
    }

    @Override
    public <U> Result<U, E> flatMap(Function<? super T, Result<U, E>> mapper) {
        return mapper.apply(value);
    }
    @Override
    public Result<T, E> peek(Consumer<? super T> consumer) {
        consumer.accept(value);
        return this;
    }
}
