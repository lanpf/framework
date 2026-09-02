package com.cloud.framework.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PersistencePropertiesTest {

    @Test
    void shouldExposeConfigurationDefaults() {
        PersistenceProperties properties = new PersistenceProperties();

        assertThat(properties.getDatabase()).isEqualTo("mysql");
        assertThat(properties.getNaming().getTablePrefix()).isNull();
        assertThat(properties.getNaming().getTableSuffix()).isEqualTo("_d_o");
    }
}
