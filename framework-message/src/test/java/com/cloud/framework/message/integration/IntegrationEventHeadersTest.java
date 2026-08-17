package com.cloud.framework.message.integration;

import java.time.Instant;
import java.util.Map;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntegrationEventHeadersTest {

    @Test
    void shouldContainOnlyIntegrationEventHeaders() {
        Map<String, Object> headers = IntegrationEventHeaders.from(new TestIntegrationEvent());

        assertEquals(4, headers.size());
        assertEquals("event-1", headers.get(IntegrationEventMessageHeaders.EVENT_ID));
        assertEquals("person.changed", headers.get(IntegrationEventMessageHeaders.EVENT_TYPE));
        assertEquals("person-1", headers.get(IntegrationEventMessageHeaders.AGGREGATE_ID));
        assertEquals("person", headers.get(IntegrationEventMessageHeaders.AGGREGATE_TYPE));
    }

    private static class TestIntegrationEvent implements IntegrationEvent {

        @Override
        public String getEventId() {
            return "event-1";
        }

        @Override
        public String getEventType() {
            return "person.changed";
        }

        @Override
        public Instant getOccurredAt() {
            return Instant.EPOCH;
        }

        @Override
        public String getAggregateType() {
            return "person";
        }

        @Override
        public String getAggregateId() {
            return "person-1";
        }
    }
}
