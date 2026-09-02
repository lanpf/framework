package com.cloud.framework.persistence.naming;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PersistenceTableNamingTest {

    @Test
    void shouldApplyPrefixAndStripExistingSuffix() {
        assertThat(PersistenceTableNaming.apply("order_d_o", "t_", "_d_o"))
                .isEqualTo("t_order");
    }

    @Test
    void shouldApplyPrefixWhenSuffixAbsent() {
        assertThat(PersistenceTableNaming.apply("order", "t_", "_d_o"))
                .isEqualTo("t_order");
    }

    @Test
    void shouldNotDuplicatePrefix() {
        assertThat(PersistenceTableNaming.apply("t_order", "t_", "_d_o"))
                .isEqualTo("t_order");
    }

    @Test
    void shouldIgnoreBlankSuffix() {
        assertThat(PersistenceTableNaming.apply("user_d_o", "t_", " "))
                .isEqualTo("t_user_d_o");
    }

    @Test
    void shouldStripSuffixWithoutPrefix() {
        assertThat(PersistenceTableNaming.apply("order_d_o", null, "_d_o"))
                .isEqualTo("order");
    }

    @Test
    void shouldKeepNullName() {
        assertThat(PersistenceTableNaming.apply(null, "t_", "_d_o")).isNull();
    }
}
