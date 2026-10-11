package com.cloud.framework.core.util;

import com.cloud.framework.core.validation.Require;

import java.util.Collection;
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
        Require.noNulls(values).forEach(v -> {
            if (consumer != null) {
                consumer.accept(v);
            }
        });
    }

    public static <T> List<T> copyToList(Collection<T> values) {
        return copyToList(values, null);
    }

    public static <T> List<T> copyToList(Collection<T> values, Consumer<T> consumer) {
        if (values == null) {
            return List.of();
        }
        forEach(values, consumer);
        return List.copyOf(values);
    }

    public static <T> Set<T> copyToSet(Collection<T> values) {
        return copyToSet(values, null);
    }

    public static <T> Set<T> copyToSet(Collection<T> values, Consumer<T> consumer) {
        if (values == null) {
            return Set.of();
        }
        forEach(values, consumer);
        return Set.copyOf(values);
    }

    public static <K, V> void forEach(Map<K, V> values, BiConsumer<K, V> consumer) {
        if (values == null) {
            return;
        }
        Require.noNulls(values).forEach((k, v) -> {
            if (consumer != null) {
                consumer.accept(k, v);
            }
        });
    }

    public static <K, V> Map<K, V> copyToMap(Map<K, V> values) {
        return copyToMap(values, null);
    }

    public static <K, V> Map<K, V> copyToMap(Map<K, V> values, BiConsumer<K, V> consumer) {
        if (values == null) {
            return Map.of();
        }
        forEach(values, consumer);
        return Map.copyOf(values);
    }
}
