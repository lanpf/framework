package com.cloud.framework.domain;

import java.util.Optional;

public interface Repository<T extends AggregateRoot<ID>, ID extends EntityId<?>> {
    ID nextId();

    void save(T aggregate);

    Optional<T> findById(ID id);

    default boolean existsById(ID id) {
        return findById(id).isPresent();
    }
}
