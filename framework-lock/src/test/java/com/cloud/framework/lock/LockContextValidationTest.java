package com.cloud.framework.lock;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LockContextValidationTest {

    private final Validator validator = Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory()
            .getValidator();

    @Test
    void shouldValidateSceneKeyAndWaitTime() {
        LockContext context = new LockContext(Duration.ofSeconds(-1), " ");

        assertEquals(2, validator.validate(context).size());
    }
}
