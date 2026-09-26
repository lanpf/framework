package com.cloud.framework.core.validation;

import com.cloud.framework.core.error.BaseException;
import com.cloud.framework.core.error.FrameworkError;
import com.cloud.framework.core.error.FrameworkException;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class Require {

    public static <T> T notNull(T value) {
        return notNull(value, FrameworkException::missingObjectField);
    }

    public static <T> T notNull(T value, Supplier<BaseException> exceptionSupplier) {
        return Optional.ofNullable(value).orElseThrow(exceptionSupplier);
    }

    public static String notBlank(String value) {
        return notBlank(value, FrameworkException::invalidObjectField);
    }

    public static String notBlank(String value, Supplier<BaseException> exceptionSupplier) {
        return Optional.ofNullable(value).filter(v -> !v.isBlank()).orElseThrow(exceptionSupplier);
    }

    public static <T extends Number> T positive(T value) {
        return positive(value, FrameworkException::invalidObjectField);
    }

    public static <T extends Number> T positive(T value, Supplier<BaseException> exceptionSupplier) {
        return Optional.ofNullable(value).filter(v -> v.longValue() > 0).orElseThrow(exceptionSupplier);
    }

    public static <T> T that(T value, Predicate<T> predicate) {
        return that(value, predicate, FrameworkException::invalidObjectField);
    }

    public static <T> T that(T value, Predicate<T> predicate, Supplier<BaseException> exceptionSupplier) {
        return Optional.ofNullable(value).filter(predicate).orElseThrow(exceptionSupplier);
    }

    private Require() {
    }

    public static <T extends Collection<?>> T notEmpty(T values) {
        return notEmpty(values, FrameworkException::invalidObjectField);
    }

    public static <T extends Collection<?>> T notEmpty(T values, Supplier<BaseException> exceptionSupplier) {
        return Optional.ofNullable(values).filter(v -> !v.isEmpty()).orElseThrow(exceptionSupplier);
    }

    public static <T> Set<T> noDuplicates(Collection<T> values) {
        return noDuplicates(values, () -> new FrameworkException(FrameworkError.COLLECTION_ELEMENT_AMBIGUOUS));
    }

    public static <T> Set<T> noDuplicates(Collection<T> values, Supplier<BaseException> exceptionSupplier) {
        Set<T> indexed = new LinkedHashSet<>();
        notNull(values).forEach(value -> that(notNull(value, FrameworkException::missingCollectionElement),
                indexed::add,
                exceptionSupplier));
        return indexed;
    }

    public static <T extends Map<?, ?>> T notEmpty(T values) {
        return notEmpty(values, FrameworkException::invalidObjectField);
    }

    public static <T extends Map<?, ?>> T notEmpty(T values, Supplier<BaseException> exceptionSupplier) {
        return Optional.ofNullable(values).filter(v -> !v.isEmpty()).orElseThrow(exceptionSupplier);
    }

    public static <K, T> Map<K, T> noDuplicates(Collection<T> values, Function<T, K> keyOperator) {
        return noDuplicates(values, keyOperator,() -> new FrameworkException(FrameworkError.COLLECTION_ELEMENT_AMBIGUOUS));
    }

    public static <K, T> Map<K, T> noDuplicates(Collection<T> values, Function<T, K> keyOperator, Supplier<BaseException> exceptionSupplier) {
        Map<K, T> indexed = new LinkedHashMap<>();
        notNull(values).forEach(value -> that(notNull(keyOperator.apply(value), FrameworkException::missingCollectionElement),
                        v -> indexed.putIfAbsent(v, value) != null,
                        exceptionSupplier));
        return indexed;
    }

}
