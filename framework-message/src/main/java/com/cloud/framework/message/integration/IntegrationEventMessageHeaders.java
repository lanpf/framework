package com.cloud.framework.message.integration;

import com.cloud.framework.message.support.MessageHeaders;

public final class IntegrationEventMessageHeaders {
    private IntegrationEventMessageHeaders() {
    }
    public static final String EVENT_ID = MessageHeaders.PREFIX + "event-id";

    public static final String EVENT_TYPE = MessageHeaders.PREFIX + "event-type";

    public static final String AGGREGATE_ID = MessageHeaders.PREFIX + "aggregate-id";

    public static final String AGGREGATE_TYPE = MessageHeaders.PREFIX + "aggregate-type";
}
