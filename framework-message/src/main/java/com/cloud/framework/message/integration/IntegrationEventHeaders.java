package com.cloud.framework.message.integration;

import java.util.HashMap;
import java.util.Map;

public final class IntegrationEventHeaders {
    private IntegrationEventHeaders() {
    }
    public static Map<String, Object> from(IntegrationEvent event) {
        Map<String, Object> headers = new HashMap<>();
        headers.put(IntegrationEventMessageHeaders.EVENT_ID, event.getEventId());
        headers.put(IntegrationEventMessageHeaders.EVENT_TYPE, event.getEventType());
        headers.put(IntegrationEventMessageHeaders.AGGREGATE_ID, event.getAggregateId());
        headers.put(IntegrationEventMessageHeaders.AGGREGATE_TYPE, event.getAggregateType());
        return headers;
    }
}
