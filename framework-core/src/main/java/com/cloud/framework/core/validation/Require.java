package com.cloud.framework.core.validation;

import com.cloud.framework.core.error.BaseException;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class Require {

    public static <T> T notNull(T value, Supplier<BaseException> exceptionSupplier) {
        return Optional.ofNullable(value).orElseThrow(exceptionSupplier);
    }

    public static String notBlank(String value, Supplier<BaseException> exceptionSupplier) {
        return Optional.ofNullable(value).filter(v -> !v.isBlank()).orElseThrow(exceptionSupplier);
    }

    public static <T extends Collection<?>> T notEmpty(T values, Supplier<BaseException> exceptionSupplier) {
        return Optional.ofNullable(values).filter(v -> !v.isEmpty()).orElseThrow(exceptionSupplier);
    }

    public static <T extends Map<?, ?>> T notEmpty(T values, Supplier<BaseException> exceptionSupplier) {
        return Optional.ofNullable(values).filter(v -> !v.isEmpty()).orElseThrow(exceptionSupplier);
    }

    public static <T extends Number> T positive(T value, Supplier<BaseException> exceptionSupplier) {
        return Optional.ofNullable(value).filter(v -> v.longValue() > 0).orElseThrow(exceptionSupplier);
    }

    public static <T> Set<T> noDuplicates(Collection<T> values, Supplier<BaseException> exceptionSupplier) {
        return noDuplicates(values, UnaryOperator.identity(), exceptionSupplier);
    }

    public static <T> Set<T> noDuplicates(Collection<T> values, UnaryOperator<T> valueOperator, Supplier<BaseException> exceptionSupplier) {
        Set<T> indexed = new LinkedHashSet<>();
        notNull(values, exceptionSupplier).forEach(value -> that(valueOperator.apply(value), indexed::add, exceptionSupplier));
        return indexed;
    }

    public static <T> T that(T value, Predicate<T> predicate, Supplier<BaseException> exceptionSupplier) {
        return Optional.ofNullable(value).filter(predicate).orElseThrow(exceptionSupplier);
    }

    private Require() {
    }

}
