package com.cloud.framework.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface DelayedOperations {
    default <T> void convertAndSend(
            @NotBlank String destination,
            @NotNull T payload,
            @NotNull Function<? super T, Duration> delaySupplier
    ) {
        convertAndSend(destination, payload, value -> null, delaySupplier);
    }

    <T> void convertAndSend(
            @NotBlank String destination,
            @NotNull T payload,
            @NotNull Function<? super T, Map<String, Object>> headersSupplier,
            @NotNull Function<? super T, Duration> delaySupplier
    );

    default <T> void convertAndSendBatch(
            @NotBlank String destination,
            @NotEmpty List<T> payloads,
            @NotNull Function<? super T, Duration> delaySupplier
    ) {
        convertAndSendBatch(destination, payloads, value -> null, delaySupplier);
    }

    <T> void convertAndSendBatch(
            @NotBlank String destination,
            @NotEmpty List<T> payloads,
            @NotNull Function<? super T, Map<String, Object>> headersSupplier,
            @NotNull Function<? super T, Duration> delaySupplier
    );
}
