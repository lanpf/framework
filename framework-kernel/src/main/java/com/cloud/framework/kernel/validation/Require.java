package com.cloud.framework.kernel.validation;

import com.cloud.framework.kernel.error.BaseException;
import com.cloud.framework.kernel.error.FrameworkException;

import java.math.BigDecimal;
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

    public static <T> T satisfies(T value, Predicate<T> condition) {
        return satisfies(value, condition, "The value must not be null and must satisfy the required condition");
    }

    public static <T> T satisfies(T value, Predicate<T> condition, String errorMessage) {
        return satisfies(value, condition, () -> FrameworkException.illegalArgument(errorMessage));
    }

    public static <T> T satisfies(T value, Predicate<T> condition, Supplier<? extends BaseException> error) {
        if (condition == null) {
            throw FrameworkException.illegalArgument("condition must not be null");
        }
        if (error == null) {
            throw FrameworkException.illegalArgument("error supplier must not be null");
        }
        return Optional.ofNullable(value).filter(condition).orElseThrow(error);
    }

    public static void state(boolean condition, String errorMessage) {
        state(condition, () -> FrameworkException.illegalState(errorMessage));
    }

    public static void state(boolean condition, Supplier<? extends FrameworkException> error) {
        if (error == null) {
            throw FrameworkException.illegalArgument("error supplier must not be null");
        }
        if (!condition) {
            FrameworkException exception = error.get();
            if (exception == null) {
                throw FrameworkException.illegalArgument("error supplier must not return null");
            }
            throw exception;
        }
    }

    public static <T> T notNull(T value) {
        return notNull(value, "The value must not be null");
    }

    public static <T> T notNull(T value, String errorMessage) {
        return notNull(value, () -> FrameworkException.illegalArgument(errorMessage));
    }

    public static <T> T notNull(T value, Supplier<? extends BaseException> error) {
        return satisfies(value, Objects::nonNull, error);
    }

    public static String notBlank(String value) {
        return notBlank(value, "The string must not be null or blank");
    }

    public static String notBlank(String value, String errorMessage) {
        return notBlank(value, () -> FrameworkException.illegalArgument(errorMessage));
    }

    public static String notBlank(String value, Supplier<? extends BaseException> error) {
        return satisfies(value, candidate -> !candidate.isBlank(), error);
    }

    public static <T extends Number> T positive(T value) {
        return positive(value, "The value must not be null and must be positive");
    }

    public static <T extends Number> T positive(T value, String errorMessage) {
        return positive(value, () -> FrameworkException.illegalArgument(errorMessage));
    }

    public static <T extends Number> T positive(T value, Supplier<? extends BaseException> error) {
        return satisfies(value, candidate -> {
            try {
                return new BigDecimal(candidate.toString()).signum() > 0;
            } catch (NumberFormatException exception) {
                return false;
            }
        }, error);
    }

    public static <T extends Collection<?>> T notEmpty(T values) {
        return notEmpty(values, "The collection must not be null or empty");
    }

    public static <T extends Collection<?>> T notEmpty(T values, String errorMessage) {
        return notEmpty(values, () -> FrameworkException.illegalArgument(errorMessage));
    }

    public static <T extends Collection<?>> T notEmpty(T values, Supplier<? extends BaseException> error) {
        return satisfies(values, candidate -> !candidate.isEmpty(), error);
    }

    public static <T extends Map<?, ?>> T notEmpty(T values) {
        return notEmpty(values, "The map must not be null or empty");
    }

    public static <T extends Map<?, ?>> T notEmpty(T values, String errorMessage) {
        return notEmpty(values, () -> FrameworkException.illegalArgument(errorMessage));
    }

    public static <T extends Map<?, ?>> T notEmpty(T values, Supplier<? extends BaseException> error) {
        return satisfies(values, candidate -> !candidate.isEmpty(), error);
    }

    public static <T extends Collection<?>> T noNulls(T values) {
        return noNulls(values, "The collection must not be null and must not contain null elements");
    }

    public static <T extends Collection<?>> T noNulls(T values, String errorMessage) {
        return noNulls(values, () -> FrameworkException.illegalArgument(errorMessage));
    }

    public static <T extends Collection<?>> T noNulls(T values, Supplier<? extends BaseException> error) {
        notNull(values, error).forEach(value -> notNull(value, error));
        return values;
    }

    public static <T extends Map<?, ?>> T noNulls(T values) {
        return noNulls(values, "The map must not be null and must not contain null keys or values");
    }

    public static <T extends Map<?, ?>> T noNulls(T values, String errorMessage) {
        return noNulls(values, () -> FrameworkException.illegalArgument(errorMessage));
    }

    public static <T extends Map<?, ?>> T noNulls(T values, Supplier<? extends BaseException> error) {
        notNull(values, error).forEach((key, value) -> {
            notNull(key, error);
            notNull(value, error);
        });
        return values;
    }

    public static <T> Set<T> noDuplicates(Collection<T> values) {
        return noDuplicates(values, "The collection must not be null and must not contain null or duplicate elements");
    }

    public static <T> Set<T> noDuplicates(Collection<T> values, String errorMessage) {
        return noDuplicates(values, () -> FrameworkException.illegalArgument(errorMessage));
    }

    public static <T> Set<T> noDuplicates(Collection<T> values, Supplier<? extends BaseException> error) {
        Set<T> indexed = new LinkedHashSet<>();
        noNulls(values, error).forEach(value -> {
            if (!indexed.add(value)) {
                throw error.get();
            }
        });
        return indexed;
    }

    public static <K, V> Map<K, V> noDuplicates(Collection<V> values, Function<V, K> keyMapper) {
        return noDuplicates(values, keyMapper,
                "The collection must not be null or contain null elements, "
                        + "and keyMapper must not return null or duplicate keys");
    }

    public static <K, V> Map<K, V> noDuplicates(
            Collection<V> values,
            Function<V, K> keyMapper,
            String errorMessage
    ) {
        return noDuplicates(values, keyMapper, () -> FrameworkException.illegalArgument(errorMessage));
    }

    public static <K, V> Map<K, V> noDuplicates(
            Collection<V> values,
            Function<V, K> keyMapper,
            Supplier<? extends BaseException> error
    ) {
        if (keyMapper == null) {
            throw FrameworkException.illegalArgument("keyMapper must not be null");
        }
        Map<K, V> indexed = new LinkedHashMap<>();
        noNulls(values, error).forEach(value -> {
            K key = notNull(keyMapper.apply(value), error);
            if (indexed.putIfAbsent(key, value) != null) {
                throw error.get();
            }
        });
        return indexed;
    }

    private Require() {
    }
}
