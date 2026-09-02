package com.cloud.framework.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import org.junit.jupiter.api.Test;

class DomainEventTest {

    private static final class OrderCreatedEvent extends AbstractDomainEvent {

        private OrderCreatedEvent(Instant occurredAt) {
            super(occurredAt);
        }
    }

    @Test
    void shouldDeriveEventTypeFromSimpleClassName() {
        OrderCreatedEvent event = new OrderCreatedEvent(Instant.EPOCH);

        assertThat(event.eventType()).isEqualTo("OrderCreatedEvent");
        assertThat(event.getEventType()).isEqualTo("OrderCreatedEvent");
    }
}
