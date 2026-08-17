package com.cloud.framework.id;

public interface LongIdGenerator extends IdGenerator<Long> {
    @Override
    Long nextId(String name);

    default long nextLongId(String name) {
        return nextId(name);
    }
}
