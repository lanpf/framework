package com.cloud.framework.core.naming;

@FunctionalInterface
public interface NamespaceResolver {

    String resolve(Namespaced namespaced);
}
