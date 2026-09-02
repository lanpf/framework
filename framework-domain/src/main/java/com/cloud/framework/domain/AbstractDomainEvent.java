package com.cloud.framework.domain;

import java.time.Instant;
import java.util.Objects;
import lombok.Getter;

@Getter
public abstract class AbstractDomainEvent implements DomainEvent {

    private final Instant occurredAt;

    protected AbstractDomainEvent(Instant occurredAt) {
        this.occurredAt = Objects.requireNonNull(occurredAt, "occurredAt must not be null");
    }
}
