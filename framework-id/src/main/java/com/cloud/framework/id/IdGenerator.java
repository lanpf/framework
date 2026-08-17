package com.cloud.framework.id;

@FunctionalInterface
public interface IdGenerator<T> {
    T nextId(String name);
}
