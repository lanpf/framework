package com.cloud.framework.message.integration;

import com.cloud.framework.kernel.validation.Require;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface IntegrationEventPublisher {
    void publish(@NotNull IntegrationEvent event);

    default void publishAll(@NotEmpty List<? extends IntegrationEvent> events) {
        Require.notEmpty(events, "events must not be null or empty");
        events.forEach(this::publish);
    }
}
