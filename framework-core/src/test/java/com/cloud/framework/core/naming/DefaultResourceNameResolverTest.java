package com.cloud.framework.core.naming;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class DefaultResourceNameResolverTest {

    private final DefaultResourceNameResolver resolver = new DefaultResourceNameResolver();

    @Test
    void shouldJoinSegmentsWithDelimiter() {
        assertThat(resolver.resolve("order", "lock", "1001")).isEqualTo("order:lock:1001");
    }

    @Test
    void shouldSkipBlankSegments() {
        assertThat(resolver.resolve("order", null, " ", "", "1001")).isEqualTo("order:1001");
    }

    @Test
    void shouldJoinToEmptyWithoutSegments() {
        assertThat(resolver.resolve()).isEmpty();
        assertThat(resolver.resolve((String) null)).isEmpty();
    }

    @Test
    void shouldExposeDefaultDelimiter() {
        assertThat(ResourceNameResolver.DELIMITER).isEqualTo(":");
        assertThat(resolver.delimiter()).isEqualTo(ResourceNameResolver.DELIMITER);
    }
}
