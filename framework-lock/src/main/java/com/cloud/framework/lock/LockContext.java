package com.cloud.framework.lock;

import com.cloud.framework.kernel.validation.Require;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.time.DurationMin;

import java.time.Duration;

public record LockContext(
        @NotNull @DurationMin Duration waitTime,
        @NotBlank String @NotEmpty ... lockNames
) {

    public LockContext {
        Require.notNull(waitTime, "waitTime must not be null");
        Require.satisfies(waitTime, value -> !value.isNegative(), "waitTime must not be negative");
        Require.satisfies(lockNames, values -> values.length > 0, "lockNames must not be empty");
        for (String lockName : lockNames) {
            Require.notBlank(lockName, "lockNames must not contain blank name");
        }
        lockNames = lockNames.clone();
    }

    public LockContext(@NotBlank String @NotEmpty ... lockNames) {
        this(Duration.ZERO, lockNames);
    }

    @Override
    public String[] lockNames() {
        return lockNames.clone();
    }
}
