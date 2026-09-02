package com.cloud.framework.id;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class LongIdGeneratorTest {

    @Test
    void shouldDelegateNextLongIdToNextId() {
        LongIdGenerator generator = name -> 42L;

        assertThat(generator.nextId("order")).isEqualTo(42L);
        assertThat(generator.nextLongId("order")).isEqualTo(42L);
    }

    @Test
    void shouldFailFastWhenGeneratorReturnsNull() {
        LongIdGenerator generator = name -> null;

        assertThatThrownBy(() -> generator.nextLongId("order"))
                .isInstanceOf(NullPointerException.class);
    }
}
