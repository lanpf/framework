package com.cloud.framework.core.util;

import com.cloud.framework.core.error.BaseException;
import com.cloud.framework.core.error.FrameworkException;
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
import java.util.function.Supplier;

public final class NullSafe {

    private static final Supplier<BaseException> NULL_ELEMENT =
            () -> FrameworkException.illegalArgument("The collection must not contain null elements");

    private static final Supplier<BaseException> NULL_KEY_OR_VALUE =
            () -> FrameworkException.illegalArgument("The map must not contain null keys or values");

    private NullSafe() {
    }

    // ---------- Collection ----------

    public static <T> void visit(Collection<T> values, Consumer<T> consumer) {
        visit(values, null, consumer);
    }

    public static <T> void visit(Collection<T> values, Supplier<BaseException> error, Consumer<T> consumer) {
        Require.notNull(consumer, "The consumer must not be null");
        if (values == null) {
            return;
        }
        visitNoNulls(values, error, consumer);
    }

    public static <T> List<T> copyToList(Collection<T> values) {
        return copyToList(values, null, null);
    }

    public static <T> List<T> copyToList(Collection<T> values, Supplier<BaseException> error) {
        return copyToList(values, error, null);
    }

    public static <T> List<T> copyToList(Collection<T> values, Supplier<BaseException> error, Consumer<T> consumer) {
        if (values == null) {
            return List.of();
        }
        visitNoNulls(values, error, consumer);
        return List.copyOf(values);
    }

    public static <T> Set<T> copyToSet(Collection<T> values) {
        return copyToSet(values, null, null);
    }

    public static <T> Set<T> copyToSet(Collection<T> values, Supplier<BaseException> error) {
        return copyToSet(values, error, null);
    }

    public static <T> Set<T> copyToSet(Collection<T> values, Supplier<BaseException> error, Consumer<T> consumer) {
        if (values == null) {
            return Set.of();
        }
        visitNoNulls(values, error, consumer);
        return Set.copyOf(values);
    }

    public static <T> Set<T> copyToOrderedSet(Collection<T> values) {
        return copyToOrderedSet(values, null, null);
    }

    public static <T> Set<T> copyToOrderedSet(Collection<T> values, Supplier<BaseException> error) {
        return copyToOrderedSet(values, error, null);
    }

    public static <T> Set<T> copyToOrderedSet(Collection<T> values, Supplier<BaseException> error, Consumer<T> consumer) {
        if (values == null) {
            return Set.of();
        }
        visitNoNulls(values, error, consumer);
        return Collections.unmodifiableSet(new LinkedHashSet<>(values));
    }

    // ---------- Map ----------

    public static <K, V> void visit(Map<K, V> values, BiConsumer<K, V> consumer) {
        visit(values, null, consumer);
    }

    public static <K, V> void visit(Map<K, V> values, Supplier<BaseException> error, BiConsumer<K, V> consumer) {
        Require.notNull(consumer, "The consumer must not be null");
        if (values == null) {
            return;
        }
        visitNoNulls(values, error, consumer);
    }

    public static <K, V> Map<K, V> copyToMap(Map<K, V> values) {
        return copyToMap(values, null, null);
    }

    public static <K, V> Map<K, V> copyToMap(Map<K, V> values, Supplier<BaseException> error) {
        return copyToMap(values, error, null);
    }

    public static <K, V> Map<K, V> copyToMap(Map<K, V> values, Supplier<BaseException> error, BiConsumer<K, V> consumer) {
        if (values == null) {
            return Map.of();
        }
        visitNoNulls(values, error, consumer);
        return Map.copyOf(values);
    }

    public static <K, V> Map<K, V> copyToOrderedMap(Map<K, V> values) {
        return copyToOrderedMap(values, null, null);
    }

    public static <K, V> Map<K, V> copyToOrderedMap(Map<K, V> values, Supplier<BaseException> error) {
        return copyToOrderedMap(values, error, null);
    }

    public static <K, V> Map<K, V> copyToOrderedMap(Map<K, V> values, Supplier<BaseException> error, BiConsumer<K, V> consumer) {
        if (values == null) {
            return Map.of();
        }
        visitNoNulls(values, error, consumer);
        return Collections.unmodifiableMap(new LinkedHashMap<>(values));
    }

    // ---------- internal ----------

    private static <T> void visitNoNulls(Collection<T> values, Supplier<BaseException> error, Consumer<T> consumer) {
        Require.noNulls(values, error != null ? error : NULL_ELEMENT);
        if (consumer != null) {
            values.forEach(consumer);
        }
    }

    private static <K, V> void visitNoNulls(Map<K, V> values, Supplier<BaseException> error, BiConsumer<K, V> consumer) {
        Require.noNulls(values, error != null ? error : NULL_KEY_OR_VALUE);
        if (consumer != null) {
            values.forEach(consumer);
        }
    }
}
