package com.cloud.framework.lock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.cloud.framework.kernel.error.FrameworkException;
import java.time.Duration;
import org.junit.jupiter.api.Test;

class LockContextTest {

    @Test
    void shouldExposeWaitTimeAndLockNames() {
        LockContext context = new LockContext(Duration.ofSeconds(3), "order", "1001");

        assertThat(context.waitTime()).isEqualTo(Duration.ofSeconds(3));
        assertThat(context.lockNames()).containsExactly("order", "1001");
    }

    @Test
    void shouldDefaultWaitTimeToZero() {
        assertThat(new LockContext("order").waitTime()).isEqualTo(Duration.ZERO);
    }

    @Test
    void shouldRejectNullWaitTime() {
        assertThatThrownBy(() -> new LockContext((Duration) null, "order"))
                .isInstanceOf(FrameworkException.class)
                .hasMessage("waitTime must not be null");
    }

    @Test
    void shouldRejectNegativeWaitTime() {
        assertThatThrownBy(() -> new LockContext(Duration.ofSeconds(-1), "order"))
                .isInstanceOf(FrameworkException.class)
                .hasMessage("waitTime must not be negative");
    }

    @Test
    void shouldRejectEmptyLockNames() {
        assertThatThrownBy(LockContext::new)
                .isInstanceOf(FrameworkException.class)
                .hasMessage("lockNames must not be empty");
    }

    @Test
    void shouldRejectNullLockNames() {
        assertThatThrownBy(() -> new LockContext((String[]) null))
                .isInstanceOf(FrameworkException.class)
                .hasMessage("lockNames must not be empty");
    }

    @Test
    void shouldRejectBlankLockName() {
        assertThatThrownBy(() -> new LockContext("order", " "))
                .isInstanceOf(FrameworkException.class)
                .hasMessage("lockNames must not contain blank name");
    }
}
