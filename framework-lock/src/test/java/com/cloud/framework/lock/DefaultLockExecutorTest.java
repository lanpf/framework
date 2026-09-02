package com.cloud.framework.lock;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DefaultLockExecutorTest {

    @Test
    void shouldResolveLockNameAndReturnCallbackResult() throws Exception {
        ReentrantLock lock = new ReentrantLock();
        DefaultLockExecutor executor = new DefaultLockExecutor(lockNames -> lock);

        Optional<String> result = executor.execute(
                new LockContext("create-order", "1001"),
                () -> "created"
        );

        assertEquals(Optional.of("created"), result);
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

    @Test
    void shouldRejectNullLockProvider() {
        assertThrows(NullPointerException.class, () -> new DefaultLockExecutor(null));
    }

    @Test
    void shouldRejectNullContext() {
        DefaultLockExecutor executor = new DefaultLockExecutor(lockNames -> new ReentrantLock());
        Callable<String> callback = () -> "created";

        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> executor.execute(null, callback)
        );
        assertEquals("context must not be null", exception.getMessage());
    }

    @Test
    void shouldRejectNullCallback() {
        DefaultLockExecutor executor = new DefaultLockExecutor(lockNames -> new ReentrantLock());

        assertThrows(
                NullPointerException.class,
                () -> executor.execute(new LockContext("create-order"), (Callable<String>) null)
        );
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
