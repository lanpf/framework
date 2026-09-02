package com.cloud.framework.lock;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.Validate;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class DefaultLockExecutor implements LockExecutor {

    private final LockProvider lockProvider;

    public DefaultLockExecutor(LockProvider lockProvider) {
        this.lockProvider = Validate.notNull(lockProvider, "lockProvider must not be null");
    }

    @Override
    public <T> Optional<T> execute(
            @NotNull @Valid LockContext context,
            @NotNull Callable<T> callable
    ) throws Exception {
        Validate.notNull(context, "context must not be null");
        Validate.notNull(callable, "callable must not be null");
        Lock lock = lockProvider.obtain(context.lockNames());
        if (!tryLock(lock, context.waitTime())) {
            return Optional.empty();
        }

        try {
            return Optional.ofNullable(callable.call());
        }
        finally {
            lock.unlock();
        }
    }

    private boolean tryLock(Lock lock, Duration waitTime) throws InterruptedException {
        if (waitTime.isZero()) {
            return lock.tryLock();
        }
        return lock.tryLock(waitTime.toNanos(), TimeUnit.NANOSECONDS);
    }
}
