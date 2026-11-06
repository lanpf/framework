package com.cloud.framework.domain;

import java.time.Instant;
import com.cloud.framework.kernel.validation.Require;
import lombok.Getter;

@Getter
public abstract class AbstractDomainEvent implements DomainEvent {

    private final Instant occurredAt;

    protected AbstractDomainEvent(Instant occurredAt) {
        this.occurredAt = Require.notNull(occurredAt, "occurredAt must not be null");
    }
}
