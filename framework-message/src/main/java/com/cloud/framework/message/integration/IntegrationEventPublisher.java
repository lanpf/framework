package com.cloud.framework.message.integration;

import com.google.common.base.Preconditions;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Objects;

public interface IntegrationEventPublisher {
    void publish(@NotNull IntegrationEvent event);

    default void publishAll(@NotEmpty List<? extends IntegrationEvent> events) {
        Objects.requireNonNull(events, "events must not be null");
        Preconditions.checkArgument(!events.isEmpty(), "events must not be empty");
        events.forEach(this::publish);
    }
}
