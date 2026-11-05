package com.cloud.framework.core.util;

import com.cloud.framework.core.error.FrameworkException;
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
}
