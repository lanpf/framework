package com.cloud.framework.core.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.cloud.framework.core.error.BaseException;
import com.cloud.framework.core.error.FrameworkException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import org.junit.jupiter.api.Test;

class NullSafeTest {

    @Test
    void shouldNormalizeNullContainersToImmutableEmptyCopies() {
        List<String> list = NullSafe.copyToList(null);
        Set<String> set = NullSafe.copyToSet(null);
        Map<String, String> map = NullSafe.copyToMap(null);
        Set<String> orderedSet = NullSafe.copyToOrderedSet(null);
        Map<String, String> orderedMap = NullSafe.copyToOrderedMap(null);

        assertThat(list).isEmpty();
        assertThat(set).isEmpty();
        assertThat(map).isEmpty();
        assertThat(orderedSet).isEmpty();
        assertThat(orderedMap).isEmpty();
        assertThatThrownBy(() -> list.add("a")).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> set.add("a")).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> map.put("k", "v")).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> orderedSet.add("a")).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> orderedMap.put("k", "v")).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldAcceptEmptyContainers() {
        assertThat(NullSafe.copyToList(List.of())).isEmpty();
        assertThat(NullSafe.copyToSet(Set.of())).isEmpty();
        assertThat(NullSafe.copyToMap(Map.of())).isEmpty();
        assertThat(NullSafe.copyToOrderedSet(Set.of())).isEmpty();
        assertThat(NullSafe.copyToOrderedMap(Map.of())).isEmpty();
    }

    @Test
    void shouldMakeImmutableCopiesIndependentOfSourceChanges() {
        List<String> sourceList = new ArrayList<>(List.of("a"));
        Set<String> sourceSet = new LinkedHashSet<>(Set.of("a"));
        Map<String, String> sourceMap = new LinkedHashMap<>(Map.of("k", "v"));
        List<String> list = NullSafe.copyToList(sourceList);
        Set<String> set = NullSafe.copyToSet(sourceSet);
        Map<String, String> map = NullSafe.copyToMap(sourceMap);

        sourceList.add("b");
        sourceSet.add("b");
        sourceMap.put("next", "value");

        assertThat(list).containsExactly("a");
        assertThat(set).containsExactly("a");
        assertThat(map).containsExactlyEntriesOf(Map.of("k", "v"));
        assertThatThrownBy(() -> list.add("b")).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> set.add("b")).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> map.put("next", "value")).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldRejectNullCollectionElements() {
        List<String> values = Arrays.asList("a", null);

        assertThatThrownBy(() -> NullSafe.copyToList(values)).isInstanceOf(FrameworkException.class);
        assertThatThrownBy(() -> NullSafe.copyToSet(values)).isInstanceOf(FrameworkException.class);
        assertThatThrownBy(() -> NullSafe.copyToOrderedSet(values)).isInstanceOf(FrameworkException.class);
    }

    @Test
    void shouldRejectNullMapKeysAndValues() {
        assertThatThrownBy(() -> NullSafe.copyToMap(Collections.singletonMap(null, "v")))
                .isInstanceOf(FrameworkException.class);
        assertThatThrownBy(() -> NullSafe.copyToMap(Collections.singletonMap("k", null)))
                .isInstanceOf(FrameworkException.class);
        assertThatThrownBy(() -> NullSafe.copyToOrderedMap(Collections.singletonMap(null, "v")))
                .isInstanceOf(FrameworkException.class);
        assertThatThrownBy(() -> NullSafe.copyToOrderedMap(Collections.singletonMap("k", null)))
                .isInstanceOf(FrameworkException.class);
    }

    @Test
    void shouldInvokeCopyConsumersForEachValidElementOrEntry() {
        List<String> visited = new ArrayList<>();
        List<String> copied = NullSafe.copyToList(List.of("a", "b"), null, visited::add);
        Set<String> visitedSet = new LinkedHashSet<>();
        Set<String> copiedSet = NullSafe.copyToSet(List.of("a", "b"), null, visitedSet::add);
        Map<String, String> visitedMap = new LinkedHashMap<>();
        Map<String, String> copiedMap = NullSafe.copyToMap(Map.of("k", "v"), null, visitedMap::put);

        assertThat(copied).containsExactlyElementsOf(visited);
        assertThat(copiedSet).containsExactlyInAnyOrderElementsOf(visitedSet);
        assertThat(copiedMap).containsExactlyEntriesOf(visitedMap);
    }

    @Test
    void shouldSkipCopyConsumersForNullContainers() {
        assertThat(NullSafe.copyToList(null, null, value -> {
            throw new AssertionError("No element exists in a null container");
        })).isEmpty();
        assertThat(NullSafe.copyToSet(null, null, value -> {
            throw new AssertionError("No element exists in a null container");
        })).isEmpty();
        assertThat(NullSafe.copyToMap(null, null, (key, value) -> {
            throw new AssertionError("No entry exists in a null container");
        })).isEmpty();
        assertThat(NullSafe.copyToOrderedSet(null, null, value -> {
            throw new AssertionError("No element exists in a null container");
        })).isEmpty();
        assertThat(NullSafe.copyToOrderedMap(null, null, (key, value) -> {
            throw new AssertionError("No entry exists in a null container");
        })).isEmpty();
    }

    @Test
    void shouldRejectInvalidContainersBeforeInvokingCopyConsumers() {
        assertThatThrownBy(() -> NullSafe.copyToList(Arrays.asList("a", null), null, value -> {
            throw new AssertionError("All elements must be checked before invoking the consumer");
        })).isInstanceOf(FrameworkException.class);
        assertThatThrownBy(() -> NullSafe.copyToSet(Arrays.asList("a", null), null, value -> {
            throw new AssertionError("All elements must be checked before invoking the consumer");
        })).isInstanceOf(FrameworkException.class);
        assertThatThrownBy(() -> NullSafe.copyToMap(Collections.singletonMap("k", null), null, (key, value) -> {
            throw new AssertionError("All entries must be checked before invoking the consumer");
        })).isInstanceOf(FrameworkException.class);
        assertThatThrownBy(() -> NullSafe.copyToOrderedSet(Arrays.asList("a", null), null, value -> {
            throw new AssertionError("All elements must be checked before invoking the consumer");
        })).isInstanceOf(FrameworkException.class);
        Map<String, String> invalidMap = new LinkedHashMap<>();
        invalidMap.put("valid", "value");
        invalidMap.put("invalid", null);
        assertThatThrownBy(() -> NullSafe.copyToOrderedMap(invalidMap, null, (key, value) -> {
            throw new AssertionError("All entries must be checked before invoking the consumer");
        })).isInstanceOf(FrameworkException.class);
    }

    @Test
    void shouldPreserveFirstEncounterOrderAndDetachOrderedSets() {
        List<String> source = new ArrayList<>(List.of("z", "a", "z", "m"));
        List<String> visited = new ArrayList<>();
        Set<String> copied = NullSafe.copyToOrderedSet(source);
        Set<String> withConsumer = NullSafe.copyToOrderedSet(source, null, visited::add);
        source.clear();

        assertThat(copied).containsExactly("z", "a", "m");
        assertThat(withConsumer).containsExactly("z", "a", "m");
        assertThat(visited).containsExactly("z", "a", "z", "m");
        assertThatThrownBy(() -> copied.add("next")).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> withConsumer.remove("z")).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldPreserveIterationOrderAndDetachOrderedMapsWithoutCopyingValues() {
        List<String> value = new ArrayList<>(List.of("value"));
        Map<String, List<String>> source = new LinkedHashMap<>();
        source.put("z", value);
        source.put("a", List.of("second"));
        source.put("m", List.of("third"));
        List<String> visited = new ArrayList<>();
        Map<String, List<String>> copied = NullSafe.copyToOrderedMap(source);
        Map<String, List<String>> withConsumer = NullSafe.copyToOrderedMap(source, null, (key, item) -> visited.add(key));
        source.clear();

        assertThat(copied.keySet()).containsExactly("z", "a", "m");
        assertThat(withConsumer.keySet()).containsExactly("z", "a", "m");
        assertThat(visited).containsExactly("z", "a", "m");
        assertThat(copied.get("z")).isSameAs(value);
        assertThatThrownBy(() -> copied.put("next", List.of())).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> withConsumer.entrySet().iterator().next().setValue(List.of()))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldUseSuppliedExceptionForInvalidCopyElementsAndEntries() {
        FrameworkException failure = FrameworkException.illegalState("copy rejected");
        List<String> invalid = Arrays.asList("a", null);

        assertThatThrownBy(() -> NullSafe.copyToList(invalid, () -> failure)).isSameAs(failure);
        assertThatThrownBy(() -> NullSafe.copyToSet(invalid, () -> failure)).isSameAs(failure);
        assertThatThrownBy(() -> NullSafe.copyToOrderedSet(invalid, () -> failure)).isSameAs(failure);
        assertThatThrownBy(() -> NullSafe.copyToMap(Collections.singletonMap(null, "v"), () -> failure))
                .isSameAs(failure);
        assertThatThrownBy(() -> NullSafe.copyToMap(Collections.singletonMap("k", null), () -> failure))
                .isSameAs(failure);
        assertThatThrownBy(() -> NullSafe.copyToOrderedMap(Collections.singletonMap(null, "v"), () -> failure))
                .isSameAs(failure);
        assertThatThrownBy(() -> NullSafe.copyToOrderedMap(Collections.singletonMap("k", null), () -> failure))
                .isSameAs(failure);
    }

    @Test
    void shouldKeepExceptionSuppliersLazyForNullEmptyAndValidCopies() {
        Supplier<BaseException> unexpectedFailure = () -> {
            throw new AssertionError("Permitted containers must not create an exception");
        };

        assertThat(NullSafe.copyToList(null, unexpectedFailure)).isEmpty();
        assertThat(NullSafe.copyToSet(null, unexpectedFailure)).isEmpty();
        assertThat(NullSafe.copyToOrderedSet(null, unexpectedFailure)).isEmpty();
        assertThat(NullSafe.copyToMap(null, unexpectedFailure)).isEmpty();
        assertThat(NullSafe.copyToOrderedMap(null, unexpectedFailure)).isEmpty();
        assertThat(NullSafe.copyToList(List.of(), unexpectedFailure)).isEmpty();
        assertThat(NullSafe.copyToSet(Set.of(), unexpectedFailure)).isEmpty();
        assertThat(NullSafe.copyToOrderedSet(Set.of(), unexpectedFailure)).isEmpty();
        assertThat(NullSafe.copyToMap(Map.of(), unexpectedFailure)).isEmpty();
        assertThat(NullSafe.copyToOrderedMap(Map.of(), unexpectedFailure)).isEmpty();
        assertThat(NullSafe.copyToList(List.of("a"), unexpectedFailure)).containsExactly("a");
        assertThat(NullSafe.copyToMap(Map.of("k", "v"), unexpectedFailure)).containsEntry("k", "v");
    }

    @Test
    void shouldUseSuppliedExceptionBeforeInvokingCopyConsumer() {
        FrameworkException failure = FrameworkException.illegalState("copy rejected");
        List<String> visited = new ArrayList<>();

        assertThatThrownBy(() -> NullSafe.copyToList(Arrays.asList("a", null), () -> failure, visited::add))
                .isSameAs(failure);
        Map<String, String> entries = new LinkedHashMap<>();
        entries.put("first", "valid");
        entries.put("second", null);
        assertThatThrownBy(() -> NullSafe.copyToMap(entries, () -> failure,
                (key, value) -> visited.add(value))).isSameAs(failure);
        assertThat(visited).isEmpty();
    }
}
