package com.ledgerlib.core;

import java.util.function.Consumer;
import java.util.function.Function;

public sealed interface Result<T, E extends Exception> permits Ok, Err {
    boolean isOk();
    boolean isErr();

    T orElse(T defaultValue);
    T orElseThrow() throws E;

    <U> Result<U, E> map(Function<? super T,? extends U> mapper);
    <U> Result<U, E> flatMap(Function<? super T, Result<U, E>> mapper);

    Result<T, E> peek(Consumer<? super T> consumer);

    /**
    ** Static methods
    **/
    static <T, E extends Exception> Result<T, E> ok(T value) {
        return new Ok<>(value);
    }

    static <T, E extends Exception> Result<T, E> err(E err) {
        return new Err<>(err);
    }
}
