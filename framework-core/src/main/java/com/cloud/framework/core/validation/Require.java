package com.cloud.framework.core.validation;

import com.cloud.framework.core.error.BaseException;
import com.cloud.framework.core.error.FrameworkError;
import com.cloud.framework.core.error.FrameworkException;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class Require {

    public static <T> T valid(T value, Predicate<T> predicate) {
        return valid(value, predicate, FrameworkException::invalidObject);
    }

    public static <T> T valid(T value, Predicate<T> predicate, Supplier<BaseException> exceptionSupplier) {
        return Optional.ofNullable(value).filter(predicate).orElseThrow(exceptionSupplier);
    }

    public static void allNotNull(Object... values) {
        for (Object v : notNull(values)) {
            notNull(v);
        }
    }

    public static void allNotBlank(String... values) {
        for (String v : notNull(values)) {
            notBlank(v);
        }
    }

    public static void allPositive(Number... values) {
        for (Number v : notNull(values)) {
            positive(v);
        }
    }

    public static <T> T notNull(T value) {
        return notNull(value, FrameworkException::missingObject);
    }

    public static <T> T notNull(T value, Supplier<BaseException> exceptionSupplier) {
        return valid(value, Objects::nonNull, exceptionSupplier);
    }

    public static String notBlank(String value) {
        return notBlank(value, FrameworkException::invalidObject);
    }

    public static String notBlank(String value, Supplier<BaseException> exceptionSupplier) {
        return valid(value, v -> !v.isBlank(), exceptionSupplier);
    }

    public static <T extends Number> T positive(T value) {
        return positive(value, FrameworkException::invalidObject);
    }

    public static <T extends Number> T positive(T value, Supplier<BaseException> exceptionSupplier) {
        return valid(value, v -> v.longValue() > 0, exceptionSupplier);
    }

    public static <T extends Collection<?>> T notEmpty(T values) {
        return notEmpty(values, FrameworkException::invalidObject);
    }

    public static <T extends Collection<?>> T notEmpty(T values, Supplier<BaseException> exceptionSupplier) {
        return valid(values, v -> !v.isEmpty(), exceptionSupplier);
    }

    public static <T extends Map<?, ?>> T notEmpty(T values) {
        return notEmpty(values, FrameworkException::invalidObject);
    }

    public static <T extends Map<?, ?>> T notEmpty(T values, Supplier<BaseException> exceptionSupplier) {
        return valid(values, v -> !v.isEmpty(), exceptionSupplier);
    }

    public static <T extends Collection<?>> T noNulls(T values) {
        return noNulls(values, FrameworkException::invalidObject);
    }

    /**
     * Rejects a null container or null elements using the supplied exception;
     * an empty collection is valid, and the original collection is returned.
     */
    public static <T extends Collection<?>> T noNulls(T values, Supplier<BaseException> exceptionSupplier) {
        notNull(values).forEach(v -> notNull(v, exceptionSupplier));
        return values;
    }

    public static <T extends Map<?, ?>> T noNulls(T values) {
        return noNulls(values, FrameworkException::invalidObject);
    }

    /**
     * Rejects a null container, key, or value using the supplied exception;
     * an empty map is valid, and the original map is returned.
     */
    public static <T extends Map<?, ?>> T noNulls(T values, Supplier<BaseException> exceptionSupplier) {
        notNull(values).forEach((k, v) -> {
            notNull(k, exceptionSupplier);
            notNull(v, exceptionSupplier);
        });
        return values;
    }

    public static <T> Set<T> noDuplicates(Collection<T> values) {
        return noDuplicates(values, () -> new FrameworkException(FrameworkError.OBJECT_AMBIGUOUS));
    }

    public static <T> Set<T> noDuplicates(Collection<T> values, Supplier<BaseException> exceptionSupplier) {
        noNulls(values);
        Set<T> indexed = new LinkedHashSet<>();
        values.forEach(v -> {
            if (!indexed.add(v)) {
                throw exceptionSupplier.get();
            }
        });
        return indexed;
    }

    public static <K, V> Map<K, V> noDuplicates(Collection<V> values, Function<V, K> keyOperator) {
        return noDuplicates(values, keyOperator, () -> new FrameworkException(FrameworkError.OBJECT_AMBIGUOUS));
    }

    public static <K, V> Map<K, V> noDuplicates(Collection<V> values, Function<V, K> keyOperator, Supplier<BaseException> exceptionSupplier) {
        noNulls(values);
        Map<K, V> indexed = new LinkedHashMap<>();
        values.forEach(v -> {
            K key = valid(keyOperator.apply(v), Objects::nonNull);
            if (indexed.putIfAbsent(key, v) != null) {
                throw exceptionSupplier.get();
            }
        });
        return indexed;
    }

    private Require() {
    }
}
