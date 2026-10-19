package com.cloud.framework.message.integration;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IntegrationEventPublisherTest {

    @Test
    void shouldRejectNullOrEmptyBatchBeforePublishing() {
        List<IntegrationEvent> published = new ArrayList<>();
        IntegrationEventPublisher publisher = published::add;

        assertThatThrownBy(() -> publisher.publishAll(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> publisher.publishAll(List.of())).isInstanceOf(IllegalArgumentException.class);
        assertThat(published).isEmpty();
    }

    @Test
    void shouldPublishBatchInInputOrder() {
        List<IntegrationEvent> published = new ArrayList<>();
        IntegrationEventPublisher publisher = published::add;
        IntegrationEvent first = new TestEvent("first");
        IntegrationEvent second = new TestEvent("second");

        publisher.publishAll(List.of(first, second));

        assertThat(published).containsExactly(first, second);
    }

    private record TestEvent(String getEventId) implements IntegrationEvent {
        @Override
        public Instant getOccurredAt() {
            return Instant.EPOCH;
        }

        @Override
        public String getAggregateType() {
            return "test";
        }

        @Override
        public String getAggregateId() {
            return "aggregate";
        }
    }
}
