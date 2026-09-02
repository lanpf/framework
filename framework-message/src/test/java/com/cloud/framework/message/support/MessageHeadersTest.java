package com.cloud.framework.message.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Map;
import org.junit.jupiter.api.Test;

class MessageHeadersTest {

    @Test
    void shouldResolvePartitionKeyFromHeaders() {
        assertThat(MessageHeaders.resolvePartitionKey(Map.of(MessageHeaders.PARTITION_KEY, "order-1001")))
                .isEqualTo("order-1001");
    }

    @Test
    void shouldRejectHeadersWithoutPartitionKey() {
        assertThatThrownBy(() -> MessageHeaders.resolvePartitionKey(Map.of("other", "value")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MessageHeaders.PARTITION_KEY + " is required");
    }

    @Test
    void shouldRejectNullPartitionArgument() {
        assertThatThrownBy(() -> MessageHeaders.resolvePartitionKey(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("partition key is required");
    }

    @Test
    void shouldPassThroughPlainPartitionArgument() {
        assertThat(MessageHeaders.resolvePartitionKey("order-1001")).isEqualTo("order-1001");
    }
}
