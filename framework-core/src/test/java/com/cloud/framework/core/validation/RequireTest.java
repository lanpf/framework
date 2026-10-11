package com.cloud.framework.core.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.cloud.framework.core.error.BaseException;
import com.cloud.framework.core.error.Error;
import com.cloud.framework.core.error.FrameworkError;
import com.cloud.framework.core.error.FrameworkException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import org.junit.jupiter.api.Test;

class RequireTest {

    private static final Error TEST_ERROR = new Error() {

        @Override
        public String getErrorCode() {
            return "100001";
        }

        @Override
        public String getErrorMessage() {
            return "test error";
        }
    };

    private static final class TestException extends BaseException {

        private TestException(String message) {
            super(TEST_ERROR, message);
        }
    }

    @Test
    void shouldReturnNotBlankValue() {
        assertThat(Require.notBlank("abc", () -> new TestException("rejected"))).isEqualTo("abc");
    }

    @Test
    void shouldThrowWhenStringIsNullOrBlank() {
        assertThatThrownBy(() -> Require.notBlank(null, () -> new TestException("rejected")))
                .isInstanceOf(TestException.class);
        assertThatThrownBy(() -> Require.notBlank(" ", () -> new TestException("rejected")))
                .isInstanceOf(TestException.class);
    }

    @Test
    void shouldReturnNotNullValue() {
        Object value = new Object();

        assertThat(Require.notNull(value, () -> new TestException("rejected"))).isSameAs(value);
        assertThatThrownBy(() -> Require.notNull(null, () -> new TestException("rejected")))
                .isInstanceOf(TestException.class);
    }

    @Test
    void shouldReturnNotEmptyCollection() {
        assertThat(Require.notEmpty(List.of("a"), () -> new TestException("rejected"))).containsExactly("a");
        assertThatThrownBy(() -> Require.notEmpty(List.of(), () -> new TestException("rejected")))
                .isInstanceOf(TestException.class);
        assertThatThrownBy(() -> Require.notEmpty((List<?>) null, () -> new TestException("rejected")))
                .isInstanceOf(TestException.class);
    }

    @Test
    void shouldReturnNotEmptyMap() {
        assertThat(Require.notEmpty(Map.of("k", "v"), () -> new TestException("rejected"))).containsOnlyKeys("k");
        assertThatThrownBy(() -> Require.notEmpty(Map.of(), () -> new TestException("rejected")))
                .isInstanceOf(TestException.class);
        assertThatThrownBy(() -> Require.notEmpty((Map<?, ?>) null, () -> new TestException("rejected")))
                .isInstanceOf(TestException.class);
    }

    @Test
    void shouldReturnPositiveNumber() {
        assertThat(Require.positive(1, () -> new TestException("rejected"))).isEqualTo(1);
        assertThatThrownBy(() -> Require.positive(0, () -> new TestException("rejected")))
                .isInstanceOf(TestException.class);
        assertThatThrownBy(() -> Require.positive(-1, () -> new TestException("rejected")))
                .isInstanceOf(TestException.class);
        assertThatThrownBy(() -> Require.positive(null, () -> new TestException("rejected")))
                .isInstanceOf(TestException.class);
    }

    @Test
    void shouldReturnOriginalContainersWithoutNullElements() {
        List<String> values = List.of("a", "b");
        Map<String, String> entries = Map.of("k", "v");
        Supplier<BaseException> unexpectedFailure = () -> {
            throw new AssertionError("Exception supplier must remain lazy for valid input");
        };

        assertThat(Require.noNulls(values)).isSameAs(values);
        assertThat(Require.noNulls(entries)).isSameAs(entries);
        assertThat(Require.noNulls(values, unexpectedFailure)).isSameAs(values);
        assertThat(Require.noNulls(entries, unexpectedFailure)).isSameAs(entries);
        assertThat(Require.noNulls(List.of(), unexpectedFailure)).isEmpty();
        assertThat(Require.noNulls(Map.of(), unexpectedFailure)).isEmpty();
    }

    @Test
    void shouldPreserveDefaultMissingObjectErrorForNullContainers() {
        assertThatThrownBy(() -> Require.noNulls((List<?>) null))
                .isInstanceOf(FrameworkException.class)
                .hasFieldOrPropertyWithValue("errorCode", FrameworkError.OBJECT_REQUIRED.errorCode());
        assertThatThrownBy(() -> Require.noNulls((Map<?, ?>) null))
                .isInstanceOf(FrameworkException.class)
                .hasFieldOrPropertyWithValue("errorCode", FrameworkError.OBJECT_REQUIRED.errorCode());
    }

    @Test
    void shouldRejectNullElementsKeysAndValuesWithDefaultInvalidObjectError() {
        assertThatThrownBy(() -> Require.noNulls(Arrays.asList("a", null)))
                .isInstanceOf(FrameworkException.class)
                .hasFieldOrPropertyWithValue("errorCode", FrameworkError.OBJECT_INVALID.errorCode());
        assertThatThrownBy(() -> Require.noNulls(Collections.singletonMap(null, "v")))
                .isInstanceOf(FrameworkException.class)
                .hasFieldOrPropertyWithValue("errorCode", FrameworkError.OBJECT_INVALID.errorCode());
        assertThatThrownBy(() -> Require.noNulls(Collections.singletonMap("k", null)))
                .isInstanceOf(FrameworkException.class)
                .hasFieldOrPropertyWithValue("errorCode", FrameworkError.OBJECT_INVALID.errorCode());
    }

    @Test
    void shouldUseSuppliedExceptionForNullCollectionAndItsElements() {
        TestException failure = new TestException("rejected");

        assertThatThrownBy(() -> Require.noNulls((List<?>) null, () -> failure)).isSameAs(failure);
        assertThatThrownBy(() -> Require.noNulls(Arrays.asList("a", null), () -> failure)).isSameAs(failure);
    }

    @Test
    void shouldUseSuppliedExceptionForNullMapAndItsKeysAndValues() {
        TestException failure = new TestException("rejected");

        assertThatThrownBy(() -> Require.noNulls((Map<?, ?>) null, () -> failure)).isSameAs(failure);
        assertThatThrownBy(() -> Require.noNulls(Collections.singletonMap(null, "v"), () -> failure))
                .isSameAs(failure);
        assertThatThrownBy(() -> Require.noNulls(Collections.singletonMap("k", null), () -> failure))
                .isSameAs(failure);
    }

    @Test
    void shouldIndexDistinctValuesInEncounterOrder() {
        Supplier<BaseException> unexpectedFailure = () -> {
            throw new AssertionError("Exception supplier must remain lazy for distinct values");
        };

        assertThat(Require.noDuplicates(List.of("b", "a"), unexpectedFailure)).containsExactly("b", "a");
        assertThat(Require.noDuplicates(List.<String>of(), unexpectedFailure)).isEmpty();
    }

    @Test
    void shouldRejectDuplicateValuesWithDefaultOrSuppliedException() {
        TestException failure = new TestException("rejected");

        assertThatThrownBy(() -> Require.noDuplicates(List.of("a", "a")))
                .isInstanceOf(FrameworkException.class)
                .hasFieldOrPropertyWithValue("errorCode", FrameworkError.OBJECT_AMBIGUOUS.errorCode());
        assertThatThrownBy(() -> Require.noDuplicates(List.of("a", "a"), () -> failure)).isSameAs(failure);
    }

    @Test
    void shouldIndexDistinctKeysInEncounterOrder() {
        Supplier<BaseException> unexpectedFailure = () -> {
            throw new AssertionError("Exception supplier must remain lazy for distinct keys");
        };
        Map<Integer, String> indexed = Require.noDuplicates(List.of("bb", "a"), String::length, unexpectedFailure);

        assertThat(indexed.keySet()).containsExactly(2, 1);
        assertThat(indexed).containsEntry(2, "bb").containsEntry(1, "a");
        assertThat(Require.noDuplicates(List.<String>of(), String::length, unexpectedFailure)).isEmpty();
    }

    @Test
    void shouldRejectDuplicateKeysWithDefaultOrSuppliedException() {
        TestException failure = new TestException("rejected");

        assertThatThrownBy(() -> Require.noDuplicates(List.of("a", "b"), String::length))
                .isInstanceOf(FrameworkException.class)
                .hasFieldOrPropertyWithValue("errorCode", FrameworkError.OBJECT_AMBIGUOUS.errorCode());
        assertThatThrownBy(() -> Require.noDuplicates(List.of("a", "b"), String::length, () -> failure))
                .isSameAs(failure);
    }

    @Test
    void shouldRejectNullInputElementsAndDerivedKeysBeforeDuplicateChecks() {
        assertThatThrownBy(() -> Require.noDuplicates((List<String>) null)).isInstanceOf(FrameworkException.class);
        assertThatThrownBy(() -> Require.noDuplicates(Arrays.asList("a", null))).isInstanceOf(FrameworkException.class);
        assertThatThrownBy(() -> Require.noDuplicates((List<String>) null, String::length))
                .isInstanceOf(FrameworkException.class);
        assertThatThrownBy(() -> Require.noDuplicates(Arrays.asList("a", null), String::length))
                .isInstanceOf(FrameworkException.class);
        Set<String> absentKeys = Collections.singleton("a");
        assertThatThrownBy(() -> Require.noDuplicates(absentKeys, value -> (String) null))
                .isInstanceOf(FrameworkException.class);
    }
}
