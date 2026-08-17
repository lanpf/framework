package com.cloud.framework.lock;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.concurrent.locks.Lock;

@FunctionalInterface
public interface LockProvider {

    @NotNull
    Lock obtain(@NotBlank String lockName);
}
