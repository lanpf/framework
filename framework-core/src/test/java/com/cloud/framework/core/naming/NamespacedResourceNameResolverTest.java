package com.cloud.framework.core.naming;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class NamespacedResourceNameResolverTest {

    private final NamespacedResourceNameResolver resolver = new NamespacedResourceNameResolver(
            Namespaced::getNamespace,
            () -> "order-service"
    );

    @Test
    void shouldPrefixNamespaceBeforeBusinessKey() {
        assertThat(resolver.resolve("lock", "1001")).isEqualTo("order-service:lock:1001");
    }

    @Test
    void shouldSkipBlankBusinessSegments() {
        assertThat(resolver.resolve(null, " ", "1001")).isEqualTo("order-service:1001");
    }

    @Test
    void shouldExposeDelimiterThroughContract() {
        assertThat(resolver.delimiter()).isEqualTo(ResourceNameResolver.DELIMITER);
    }
}
