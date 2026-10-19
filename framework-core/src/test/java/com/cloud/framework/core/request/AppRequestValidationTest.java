package com.cloud.framework.core.request;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AppRequestValidationTest {

    private final Validator validator = Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory()
            .getValidator();

    @Test
    void shouldRequireAppIdOnAppRequest() {
        AppRequest request = new AppRequest(" ", null, null);

        assertThat(violationPaths(request)).containsExactly("appId");
    }

    @Test
    void shouldRequireClientIdOnClientRequest() {
        ClientAppRequest request = new ClientAppRequest("app", null, null, " ");

        assertThat(violationPaths(request)).containsExactly("clientId");
    }

    @Test
    void shouldRequireUserAndSessionOnAuthenticatedRequest() {
        AuthenticatedSessionAppRequest request =
                new AuthenticatedSessionAppRequest("app", null, null, "", "s-1");

        assertThat(violationPaths(request)).containsExactly("userId");
    }

    @Test
    void shouldRequireAllContextsOnCombinedRequest() {
        AuthenticatedSessionClientAppRequest request =
                new AuthenticatedSessionClientAppRequest("app", null, null, "", "", null);

        assertThat(violationPaths(request)).containsExactlyInAnyOrder("userId", "sessionId", "clientId");
    }

    @Test
    void shouldAcceptFullyPopulatedCombinedRequest() {
        AuthenticatedSessionClientAppRequest request =
                new AuthenticatedSessionClientAppRequest("app", "ios", "1.0.0", "u-1", "s-1", "app-store");

        assertThat(validator.validate(request)).isEmpty();
    }

    private Set<String> violationPaths(Object request) {
        return validator.validate(request).stream()
                .map(violation -> violation.getPropertyPath().toString())
                .collect(Collectors.toSet());
    }
}
