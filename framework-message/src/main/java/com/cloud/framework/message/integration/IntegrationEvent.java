package com.cloud.framework.message.integration;

import java.io.Serializable;
import java.time.Instant;

public interface IntegrationEvent extends Serializable {
    default String eventId() {
        return getEventId();
    }

    default String eventType() {
        return getEventType();
    }

    default Instant occurredAt() {
        return getOccurredAt();
    }

    default String aggregateType() {
        return getAggregateType();
    }

    default String aggregateId() {
        return getAggregateId();
    }

    String getEventId();

    default String getEventType() {
        return getClass().getSimpleName();
    }

    Instant getOccurredAt();

    String getAggregateType();

    String getAggregateId();

}
