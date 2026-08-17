package com.cloud.framework.message.integration;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface IntegrationEventPublisher {
    void publish(@NotNull IntegrationEvent event);

    default void publishAll(@NotEmpty List<? extends IntegrationEvent> events) {
        if (events == null || events.isEmpty()) {
            return;
        }
        events.forEach(this::publish);
    }
}
