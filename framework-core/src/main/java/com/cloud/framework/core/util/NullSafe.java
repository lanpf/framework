package com.cloud.framework.core.util;

import com.cloud.framework.core.validation.Require;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class NullSafe {
    private NullSafe() {
    }

    public static <T> void forEach(Collection<T> values, Consumer<T> consumer) {
        if (values == null) {
            return;
        }
        visitNonNull(values, consumer);
    }

    public static <T> List<T> copyToList(Collection<T> values) {
        return copyToList(values, null);
    }

    public static <T> List<T> copyToList(Collection<T> values, Consumer<T> consumer) {
        if (values == null) {
            return List.of();
        }
        visitNonNull(values, consumer);
        return List.copyOf(values);
    }

    public static <T> Set<T> copyToSet(Collection<T> values) {
        return copyToSet(values, null);
    }

    public static <T> Set<T> copyToSet(Collection<T> values, Consumer<T> consumer) {
        if (values == null) {
            return Set.of();
        }
        visitNonNull(values, consumer);
        return Set.copyOf(values);
    }

    /**
     * Returns an independent, unmodifiable shallow copy in first-encounter order, without sorting.
     * Duplicate elements retain their first position; null elements are rejected.
     *
     * @param values may be null, equivalent to an empty collection
     */
    public static <T> Set<T> copyToOrderedSet(Collection<T> values) {
        return copyToOrderedSet(values, null);
    }

    /**
     * Copies in first-encounter order after validating all elements and invoking the optional consumer
     * for each input element, including duplicates. The returned shallow copy is unmodifiable.
     *
     * @param values may be null, equivalent to an empty collection; null elements are rejected
     * @param consumer may be null; never invoked for a null container or when element validation fails
     */
    public static <T> Set<T> copyToOrderedSet(Collection<T> values, Consumer<T> consumer) {
        if (values == null) {
            return Set.of();
        }
        visitNonNull(values, consumer);
        return Collections.unmodifiableSet(new LinkedHashSet<>(values));
    }

    public static <K, V> void forEach(Map<K, V> values, BiConsumer<K, V> consumer) {
        if (values == null) {
            return;
        }
        visitNonNull(values, consumer);
    }

    public static <K, V> Map<K, V> copyToMap(Map<K, V> values) {
        return copyToMap(values, null);
    }

    public static <K, V> Map<K, V> copyToMap(Map<K, V> values, BiConsumer<K, V> consumer) {
        if (values == null) {
            return Map.of();
        }
        visitNonNull(values, consumer);
        return Map.copyOf(values);
    }

    /**
     * Returns an independent, unmodifiable shallow copy preserving the input's iteration order,
     * without sorting. Null keys and values are rejected.
     *
     * @param values may be null, equivalent to an empty map
     */
    public static <K, V> Map<K, V> copyToOrderedMap(Map<K, V> values) {
        return copyToOrderedMap(values, null);
    }

    /**
     * Preserves the input's iteration order after validating all entries and invoking the optional
     * consumer for each entry. The returned shallow copy is independent and unmodifiable.
     *
     * @param values may be null, equivalent to an empty map; null keys and values are rejected
     * @param consumer may be null; never invoked for a null container or when entry validation fails
     */
    public static <K, V> Map<K, V> copyToOrderedMap(Map<K, V> values, BiConsumer<K, V> consumer) {
        if (values == null) {
            return Map.of();
        }
        visitNonNull(values, consumer);
        return Collections.unmodifiableMap(new LinkedHashMap<>(values));
    }

    private static <T> void visitNonNull(Collection<T> values, Consumer<T> consumer) {
        Require.noNulls(values);
        if (consumer != null) {
            values.forEach(consumer);
        }
    }

    private static <K, V> void visitNonNull(Map<K, V> values, BiConsumer<K, V> consumer) {
        Require.noNulls(values);
        if (consumer != null) {
            values.forEach(consumer);
        }
    }
}
