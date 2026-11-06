package com.cloud.framework.core.util;

import com.cloud.framework.kernel.error.FrameworkError;
import com.cloud.framework.kernel.error.FrameworkException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NullSafeTraversalTest {

    @Test
    void shouldSkipNullContainersAndVisitValidInput() {
        List<String> visited = new ArrayList<>();
        NullSafe.visit((List<String>) null, visited::add);
        NullSafe.visit((Map<String, String>) null, (key, value) -> visited.add(value));
        assertThat(visited).isEmpty();

        NullSafe.visit(List.of("a", "b"), visited::add);
        assertThat(visited).containsExactly("a", "b");
        NullSafe.visit(Map.of("key", "c"), (key, value) -> visited.add(value));
        assertThat(visited).containsExactly("a", "b", "c");
    }

    @Test
    void shouldValidateAllElementsBeforeCallingConsumer() {
        List<String> visited = new ArrayList<>();
        assertThatThrownBy(() -> NullSafe.visit(Arrays.asList("a", null), visited::add))
                .isInstanceOf(FrameworkException.class);
        Map<String, String> entries = new LinkedHashMap<>();
        entries.put("first", "valid");
        entries.put("second", null);
        assertThatThrownBy(() -> NullSafe.visit(entries, (key, value) -> visited.add(value)))
                .isInstanceOf(FrameworkException.class);
        assertThat(visited).isEmpty();
    }

    @Test
    void shouldRejectMissingConsumerEvenForNullContainers() {
        assertThatThrownBy(() -> NullSafe.visit((List<String>) null, null))
                .isInstanceOf(FrameworkException.class)
                .hasFieldOrPropertyWithValue("errorCode", FrameworkError.ILLEGAL_ARGUMENT.errorCode())
                .hasMessage("The consumer must not be null");
        assertThatThrownBy(() -> NullSafe.visit((Map<String, String>) null, null))
                .isInstanceOf(FrameworkException.class)
                .hasFieldOrPropertyWithValue("errorCode", FrameworkError.ILLEGAL_ARGUMENT.errorCode())
                .hasMessage("The consumer must not be null");
    }

    @Test
    void shouldUseSuppliedExceptionBeforeVisitingInvalidInput() {
        FrameworkException failure = FrameworkException.illegalState("visit rejected");
        List<String> visited = new ArrayList<>();

        assertThatThrownBy(() -> NullSafe.visit(Arrays.asList("a", null), () -> failure, visited::add))
                .isSameAs(failure);
        Map<String, String> entries = new LinkedHashMap<>();
        entries.put("first", "valid");
        entries.put("second", null);
        assertThatThrownBy(() -> NullSafe.visit(entries, () -> failure,
                (key, value) -> visited.add(value))).isSameAs(failure);
        assertThat(visited).isEmpty();
    }
}
