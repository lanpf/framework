package com.cloud.framework.lock;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.time.DurationMin;

import java.time.Duration;

public record LockContext(
        @NotNull @DurationMin Duration waitTime,
        @NotBlank String @NotEmpty ... lockNames
) {
    public LockContext(@NotBlank String @NotEmpty ... lockNames) {
        this(Duration.ZERO, lockNames);
    }
}
