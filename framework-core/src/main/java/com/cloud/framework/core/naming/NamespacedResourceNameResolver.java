package com.cloud.framework.core.naming;

import lombok.RequiredArgsConstructor;

/** 组合实现：在业务键段前注入 {@link Namespaced} 解析出的命名空间前缀。 */
@RequiredArgsConstructor
public class NamespacedResourceNameResolver implements ResourceNameResolver {

    private final NamespaceResolver namespaceResolver;
    private final Namespaced namespaced;

    @Override
    public String resolve(String... names) {
        return namespaceResolver.resolve(namespaced)
                + delimiter()
                + String.join(delimiter(), normalize(names));
    }
}
