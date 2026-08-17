package com.cloud.framework.domain;

import java.util.List;

@FunctionalInterface
public interface DomainEventStore {
    void appendAll(List<? extends DomainEvent> domainEvents);
}
