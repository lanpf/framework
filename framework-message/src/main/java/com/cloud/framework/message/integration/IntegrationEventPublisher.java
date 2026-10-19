package com.cloud.framework.message.integration;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.Validate;

import java.util.List;

public interface IntegrationEventPublisher {
    void publish(@NotNull IntegrationEvent event);

    default void publishAll(@NotEmpty List<? extends IntegrationEvent> events) {
        Validate.notEmpty(events, "events must not be null or empty");
        events.forEach(this::publish);
    }
}
