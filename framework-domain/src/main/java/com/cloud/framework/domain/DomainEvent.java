package com.cloud.framework.domain;

import java.time.Instant;

public interface DomainEvent {
    default Instant occurredAt() {
        return getOccurredAt();
    }

    default String eventType() {
        return getEventType();
    }

    Instant getOccurredAt();

    default String getEventType() {
        return getClass().getSimpleName();
    }
}
