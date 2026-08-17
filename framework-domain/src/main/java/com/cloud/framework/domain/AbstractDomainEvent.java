package com.cloud.framework.domain;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public abstract class AbstractDomainEvent implements DomainEvent {
    @NonNull
    private final DomainEventId eventId;

    @NonNull
    private final Instant occurredAt;
}
