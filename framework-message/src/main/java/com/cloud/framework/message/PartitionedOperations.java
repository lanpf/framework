package com.cloud.framework.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface PartitionedOperations {
    default <T> void convertAndSend(
            @NotBlank String destination,
            @NotNull T payload,
            @NotNull Function<? super T, ?> partitionArgSupplier
    ) {
        convertAndSend(destination, payload, value -> null, partitionArgSupplier);
    }

    <T> void convertAndSend(
            @NotBlank String destination,
            @NotNull T payload,
            @NotNull Function<? super T, Map<String, Object>> headersSupplier,
            @NotNull Function<? super T, ?> partitionArgSupplier
    );

    default <T> void convertAndSendBatch(
            @NotBlank String destination,
            @NotEmpty List<T> payloads,
            @NotNull Function<? super T, ?> partitionArgSupplier
    ) {
        convertAndSendBatch(destination, payloads, value -> null, partitionArgSupplier);
    }

    <T> void convertAndSendBatch(
            @NotBlank String destination,
            @NotEmpty List<T> payloads,
            @NotNull Function<? super T, Map<String, Object>> headersSupplier,
            @NotNull Function<? super T, ?> partitionArgSupplier
    );
}
