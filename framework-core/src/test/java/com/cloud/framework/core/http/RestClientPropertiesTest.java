package com.cloud.framework.core.http;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.time.Duration;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.Test;

class RestClientPropertiesTest {

    private final Validator validator = Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory()
            .getValidator();

    @Test
    void shouldExposeTimeoutDefaults() {
        RestClientProperties properties = new RestClientProperties();

        assertThat(properties.getConnectTimeout()).isEqualTo(Duration.ofSeconds(2));
        assertThat(properties.getReadTimeout()).isEqualTo(Duration.ofSeconds(5));
    }

    @Test
    void shouldAcceptCustomTimeouts() {
        RestClientProperties properties = new RestClientProperties();
        properties.setConnectTimeout(Duration.ofSeconds(3));
        properties.setReadTimeout(Duration.ofSeconds(10));

        assertThat(validator.validate(properties)).isEmpty();
    }

    @Test
    void shouldRejectZeroConnectTimeout() {
        RestClientProperties properties = new RestClientProperties();
        properties.setConnectTimeout(Duration.ZERO);

        assertThat(validator.validate(properties)).isNotEmpty();
    }

    @Test
    void shouldRejectNullReadTimeout() {
        RestClientProperties properties = new RestClientProperties();
        properties.setReadTimeout(null);

        assertThat(validator.validate(properties)).isNotEmpty();
    }
}
