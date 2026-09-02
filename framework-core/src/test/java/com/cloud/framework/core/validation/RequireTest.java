package com.cloud.framework.core.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.cloud.framework.core.error.BaseException;
import com.cloud.framework.core.error.Error;
import java.util.List;
import java.util.Map;
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
}
