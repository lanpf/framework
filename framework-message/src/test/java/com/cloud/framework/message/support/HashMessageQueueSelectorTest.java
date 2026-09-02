package com.cloud.framework.message.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Map;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class HashMessageQueueSelectorTest {

    private final HashMessageQueueSelector selector = new HashMessageQueueSelector();

    @Test
    void shouldSelectStablePartitionWithinBounds() {
        Map<String, Object> headers = Map.of(MessageHeaders.PARTITION_KEY, "order-1001");

        Integer selected = selector.select(4, headers);

        assertThat(selected).isBetween(0, 3);
        assertThat(selector.select(4, headers)).isEqualTo(selected);
    }

    @Test
    void shouldSpreadDistinctKeysAcrossPartitions() {
        long distinctPartitions = IntStream.range(0, 32)
                .map(i -> selector.select(4, Map.of(MessageHeaders.PARTITION_KEY, "key-" + i)))
                .distinct()
                .count();

        assertThat(distinctPartitions).isGreaterThan(1);
    }

    @Test
    void shouldSupportPlainPartitionArgument() {
        assertThat(selector.select(3, "order-1001")).isBetween(0, 2);
    }

    @Test
    void shouldRejectMissingPartitionKey() {
        assertThatThrownBy(() -> selector.select(4, Map.of()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
