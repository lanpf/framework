package com.cloud.framework.core.naming;

/** 解析 {@link Namespaced} 载体的命名空间前缀。 */
@FunctionalInterface
public interface NamespaceResolver {

    String resolve(Namespaced namespaced);
}
