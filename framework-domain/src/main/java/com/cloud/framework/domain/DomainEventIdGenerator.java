package com.cloud.framework.domain;

@FunctionalInterface
public interface DomainEventIdGenerator {
    DomainEventId nextId();
}
