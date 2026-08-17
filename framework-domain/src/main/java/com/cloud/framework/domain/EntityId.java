package com.cloud.framework.domain;

import lombok.Getter;

import java.util.Objects;

@Getter
public abstract class EntityId<T> {
    private final T value;

    protected EntityId(T value) {
        if (value == null) {
            throw invalidException(value);
        }
        if (value instanceof Number && ((Number) value).longValue() <= 0) {
            throw invalidException(value);
        }
        if (value instanceof CharSequence && value.toString().trim().isEmpty()) {
            throw invalidException(value);
        }
        this.value = value;
    }

    protected RuntimeException invalidException(T value) {
        return new IllegalArgumentException("Entity id is invalid: " + value);
    }

    public T value() {
        return getValue();
    }

    @Override
    public final boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        EntityId<?> entityId = (EntityId<?>) other;
        return Objects.equals(value, entityId.value);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
