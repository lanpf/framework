package com.cloud.framework.lock;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.time.DurationMin;

import java.time.Duration;

public record LockContext(@NotBlank String scene, @NotBlank String key, @NotNull @DurationMin Duration waitTime) {

    public LockContext(@NotBlank String scene, @NotBlank String key) {
        this(scene, key, Duration.ZERO);
    }

    public String getLockName() {
        return scene + ":" + key;
    }
}
