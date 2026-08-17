package com.cloud.framework.core.naming;

@FunctionalInterface
public interface ResourceNameResolver {

    String resolve(String name);
}
