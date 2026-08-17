package com.cloud.framework.message.support;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConsistentHashMessageQueueSelectorTest {

    private final MessageQueueSelector selector = new ConsistentHashMessageQueueSelector();

    @Test
    void shouldSelectDeterministicQueueWithinPartitionRange() {
        Integer first = this.selector.select(4, "aggregate-1");
        Integer second = this.selector.select(4, "aggregate-1");

        assertEquals(first, second);
        assertTrue(first >= 0);
        assertTrue(first < 4);
    }

    @Test
    void shouldResolvePartitionKeyFromHeaders() {
        Integer direct = this.selector.select(4, "aggregate-1");
        Integer fromHeaders = this.selector.select(
                4,
                java.util.Map.of(MessageHeaders.PARTITION_KEY, "aggregate-1")
        );

        assertEquals(direct, fromHeaders);
    }
}