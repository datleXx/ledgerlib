package com.ledgerlib.core;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class AccountBalanceCollector implements Collector<Entry, Map<AccountId, BigDecimal>, Map<AccountId, Money>> {

    @Override
    public Supplier<Map<AccountId, BigDecimal>> supplier() {
        return HashMap::new;
    }

    @Override
    public BiConsumer<Map<AccountId, BigDecimal>, Entry> accumulator() {
        return (map, entry) -> {
            BigDecimal amount = entry.amount().amount();
            if (entry.side() == EntrySide.DEBIT) {
                amount = amount.negate();
            }
            map.merge(entry.account(), amount, BigDecimal::add);
        };
    }

    @Override
    public BinaryOperator<Map<AccountId, BigDecimal>> combiner() {
        return (map1, map2) -> {
            map2.forEach((key, value) -> map1.merge(key, value, BigDecimal::add));
            return map1;
        };
    }

    @Override
    public Function<Map<AccountId, BigDecimal>, Map<AccountId, Money>> finisher() {
        return map -> {
           Map<AccountId, Money> result = new HashMap<>();
           map.forEach((key, value) -> {
               result.put(key, Money.of(value.toPlainString(), "USD"));
           });
           return result;
        };
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of(Characteristics.UNORDERED);
    }
}
