package com.cloud.framework.message.support;

import com.cloud.framework.kernel.error.FrameworkError;
import com.cloud.framework.kernel.error.FrameworkException;
import com.cloud.framework.message.error.MessageError;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MessageQueueSelectorContractTest {

    private static Stream<MessageQueueSelector> selectors() {
        return Stream.of(new HashMessageQueueSelector(), new ConsistentHashMessageQueueSelector());
    }

    @ParameterizedTest
    @MethodSource("selectors")
    void shouldRejectNullPartitionCountBeforeResolvingKey(MessageQueueSelector selector) {
        assertThatThrownBy(() -> selector.select(null, null))
                .isInstanceOf(FrameworkException.class)
                .hasFieldOrPropertyWithValue("errorCode", FrameworkError.ILLEGAL_ARGUMENT.errorCode())
                .hasMessage("partitions must be positive");
    }

    @ParameterizedTest
    @MethodSource("selectors")
    void shouldRejectNonPositivePartitionCountBeforeResolvingKey(MessageQueueSelector selector) {
        assertThatThrownBy(() -> selector.select(0, null))
                .isInstanceOf(FrameworkException.class).hasMessage("partitions must be positive");
        assertThatThrownBy(() -> selector.select(-1, null))
                .isInstanceOf(FrameworkException.class).hasMessage("partitions must be positive");
    }

    @ParameterizedTest
    @MethodSource("selectors")
    void shouldRejectMissingPartitionKeyAfterValidatingCount(MessageQueueSelector selector) {
        assertThatThrownBy(() -> selector.select(4, null))
                .isInstanceOf(FrameworkException.class)
                .hasFieldOrPropertyWithValue("errorCode", MessageError.PARTITION_KEY_MISSING.errorCode())
                .hasMessage("partition key is required");
        assertThatThrownBy(() -> selector.select(4, Map.of()))
                .isInstanceOf(FrameworkException.class)
                .hasFieldOrPropertyWithValue("errorCode", MessageError.PARTITION_KEY_MISSING.errorCode())
                .hasMessage("x-partition-key is required");
    }

    @ParameterizedTest
    @MethodSource("selectors")
    void shouldResolveTheSameKeyFromPlainAndHeaderInputs(MessageQueueSelector selector) {
        Integer direct = selector.select(4, "order-1001");

        assertThat(direct).isBetween(0, 3);
        assertThat(selector.select(4, Map.of(MessageHeaders.PARTITION_KEY, "order-1001")))
                .isEqualTo(direct);
    }
}
