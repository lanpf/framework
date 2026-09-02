package com.cloud.framework.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;
import org.junit.jupiter.api.Test;

class AbstractDomainEventTest {

    private static final class OrderCreatedEvent extends AbstractDomainEvent {

        private OrderCreatedEvent(Instant occurredAt) {
            super(occurredAt);
        }
    }

    @Test
    void shouldExposeExplicitOccurredAt() {
        Instant occurredAt = Instant.parse("2026-01-01T00:00:00Z");

        OrderCreatedEvent event = new OrderCreatedEvent(occurredAt);

        assertThat(event.occurredAt()).isEqualTo(occurredAt);
        assertThat(event.getOccurredAt()).isEqualTo(occurredAt);
    }

    @Test
    void shouldRejectNullOccurredAt() {
        assertThatThrownBy(() -> new OrderCreatedEvent(null))
                .isInstanceOf(NullPointerException.class);
    }
}
