package com.cloud.framework.domain;

public interface AggregateRoot<ID extends EntityId<?>> {
    default ID id() {
        return getId();
    }

    ID getId();
}
