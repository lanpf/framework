package com.cloud.framework.lock;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultLockExecutorTest {

    @Test
    void shouldResolveLockNameAndReturnCallbackResult() throws Exception {
        AtomicReference<String> obtainedLockName = new AtomicReference<>();
        ReentrantLock lock = new ReentrantLock();
        DefaultLockExecutor executor = new DefaultLockExecutor(lockName -> {
            obtainedLockName.set(lockName);
            return lock;
        });

        Optional<String> result = executor.execute(
                new LockContext("create-order", "1001"),
                () -> "created"
        );

        assertEquals(Optional.of("created"), result);
        assertEquals("create-order:1001", obtainedLockName.get());
        assertFalse(lock.isLocked());
    }

    @Test
    void shouldNotRunCallbackWhenLockCannotBeAcquired() throws Exception {
        AtomicBoolean callbackCalled = new AtomicBoolean();
        DefaultLockExecutor executor = new DefaultLockExecutor(lockName -> new UnavailableLock());

        boolean executed = executor.execute(new LockContext("create-order", "1001"), () -> {
            callbackCalled.set(true);
        });

        assertFalse(executed);
        assertFalse(callbackCalled.get());
    }

    @Test
    void shouldReleaseLockWhenCallbackFails() {
        ReentrantLock lock = new ReentrantLock();
        DefaultLockExecutor executor = new DefaultLockExecutor(lockName -> lock);

        assertThrows(IllegalStateException.class, () -> executor.execute(
                new LockContext("create-order", "1001"),
                () -> {
                    throw new IllegalStateException("failed");
                }
        ));
        assertFalse(lock.isLocked());
    }

    private static final class UnavailableLock extends ReentrantLock {

        @Override
        public boolean tryLock() {
            return false;
        }

        @Override
        public boolean tryLock(long timeout, java.util.concurrent.TimeUnit unit) {
            return false;
        }
    }
}
