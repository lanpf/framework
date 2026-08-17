package com.cloud.framework.message.integration;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public interface IntegrationEventOutbox {
    void appendAll(@NotEmpty List<? extends IntegrationEvent> events);
}
