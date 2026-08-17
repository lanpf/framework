package com.cloud.framework.core.http;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.time.DurationMin;

import java.time.Duration;

@Getter
@Setter
public class RestClientProperties {

    @NotNull
    @DurationMin(seconds = 1)
    private Duration connectTimeout = Duration.ofSeconds(2);

    @NotNull
    @DurationMin(seconds = 1)
    private Duration readTimeout = Duration.ofSeconds(5);
}
