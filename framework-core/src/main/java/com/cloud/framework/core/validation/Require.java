package com.cloud.framework.core.validation;

import com.cloud.framework.core.error.BaseException;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public final class Require {

    public static String notBlank(String value, Supplier<BaseException> exceptionSupplier) {
        return Optional.ofNullable(value).filter(v -> !v.isBlank()).orElseThrow(exceptionSupplier);
    }

    public static <T> T notNull(T value, Supplier<BaseException> exceptionSupplier) {
        return Optional.ofNullable(value).orElseThrow(exceptionSupplier);
    }

    public static <T extends Collection<?>> T notEmpty(T value, Supplier<BaseException> exceptionSupplier) {
        return Optional.ofNullable(value).filter(v -> !v.isEmpty()).orElseThrow(exceptionSupplier);
    }

    public static <T extends Map<?, ?>> T notEmpty(T value, Supplier<BaseException> exceptionSupplier) {
        return Optional.ofNullable(value).filter(v -> !v.isEmpty()).orElseThrow(exceptionSupplier);
    }

    public static <T extends Number> T positive(T value, Supplier<BaseException> exceptionSupplier) {
        return Optional.ofNullable(value).filter(v -> v.longValue() > 0).orElseThrow(exceptionSupplier);
    }

    private Require() {
    }

}
