package com.cloud.framework.domain;

import java.time.Instant;

public interface DomainEvent {
    default DomainEventId eventId() {
        return getEventId();
    }

    default Instant occurredAt() {
        return getOccurredAt();
    }

    default String eventType() {
        return getEventType();
    }

    DomainEventId getEventId();

    Instant getOccurredAt();

    default String getEventType() {
        return getClass().getSimpleName();
    }
}
