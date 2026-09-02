package com.cloud.framework.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class EntityIdTest {

    private static final class OrderId extends EntityId<String> {

        private OrderId(String value) {
            super(value);
        }
    }

    private static final class ShipmentId extends EntityId<String> {

        private ShipmentId(String value) {
            super(value);
        }
    }

    private static final class CheckedOrderId extends EntityId<String> {

        private CheckedOrderId(String value) {
            super(value);
        }

        @Override
        protected String validate(String value) {
            if (value == null || value.isBlank()) {
                throw new IllegalArgumentException("value must not be blank");
            }
            return value;
        }
    }

    @Test
    void shouldUseValueEqualityAcrossInstances() {
        assertThat(new OrderId("1001")).isEqualTo(new OrderId("1001"));
        assertThat(new OrderId("1001")).hasSameHashCodeAs(new OrderId("1001"));
        assertThat(new OrderId("1001")).isNotEqualTo(new OrderId("1002"));
    }

    @Test
    void shouldNotEqualDifferentIdTypeWithSameValue() {
        assertThat(new OrderId("1001")).isNotEqualTo(new ShipmentId("1001"));
    }

    @Test
    void shouldExposeValueThroughBothAccessors() {
        OrderId id = new OrderId("1001");

        assertThat(id.value()).isEqualTo("1001");
        assertThat(id.getValue()).isEqualTo("1001");
        assertThat(id).hasToString("1001");
    }

    @Test
    void shouldAllowValidateHookToRejectValue() {
        assertThatThrownBy(() -> new CheckedOrderId(" "))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
