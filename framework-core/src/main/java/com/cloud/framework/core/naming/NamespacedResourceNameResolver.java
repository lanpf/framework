package com.cloud.framework.core.naming;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NamespacedResourceNameResolver implements ResourceNameResolver {

    private final NamespaceResolver namespaceResolver;
    private final Namespaced namespaced;

    @Override
    public String resolve(String name) {
        return namespaceResolver.resolve(namespaced) + ":" + name;
    }
}
