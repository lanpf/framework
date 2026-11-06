package com.cloud.framework.lock;

import com.cloud.framework.kernel.validation.Require;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;
import java.util.concurrent.Callable;

@FunctionalInterface
public interface LockExecutor {

    <T> Optional<T> execute(@NotNull @Valid LockContext context, @NotNull Callable<T> callable) throws Exception;

    default boolean execute(
            @NotNull @Valid LockContext context,
            @NotNull ThrowingRunnable runnable
    ) throws Exception {
        Require.notNull(runnable, "runnable must not be null");
        return execute(context, () -> {
            runnable.run();
            return Boolean.TRUE;
        }).orElse(false);
    }

    @FunctionalInterface
    interface ThrowingRunnable {

        void run() throws Exception;
    }
}
