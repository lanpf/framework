package com.cloud.framework.lock;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hibernate.validator.constraints.time.DurationMin;

import java.time.Duration;

public record LockContext(
        @NotNull @DurationMin Duration waitTime,
        @NotBlank String @NotEmpty ... lockNames
) {

    public LockContext {
        Validate.notNull(waitTime, "waitTime must not be null");
        Validate.isTrue(!waitTime.isNegative(), "waitTime must not be negative");
        Validate.isTrue(ArrayUtils.isNotEmpty(lockNames), "lockNames must not be empty");
        Validate.isTrue(StringUtils.isNoneBlank(lockNames), "lockNames must not contain blank name");
    }

    public LockContext(@NotBlank String @NotEmpty ... lockNames) {
        this(Duration.ZERO, lockNames);
    }
}
