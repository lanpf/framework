package com.cloud.framework.core;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.Test;

class ClientRequestValidationTest {

    private final Validator validator = Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory()
            .getValidator();

    @Test
    void shouldRequireClientAppIdOnClientRequest() {
        ClientRequest request = new ClientRequest(" ", null, null);

        assertThat(violationPaths(request)).containsExactly("clientAppId");
    }

    @Test
    void shouldRequireChannelCodeOnChannelRequest() {
        ChannelClientRequest request = new ChannelClientRequest("app", null, null, " ");

        assertThat(violationPaths(request)).containsExactly("channelCode");
    }

    @Test
    void shouldRequireUserAndSessionOnAuthenticatedRequest() {
        AuthenticatedSessionClientRequest request =
                new AuthenticatedSessionClientRequest("app", null, null, "", "s-1");

        assertThat(violationPaths(request)).containsExactly("userId");
    }

    @Test
    void shouldRequireAllContextsOnCombinedRequest() {
        AuthenticatedSessionChannelClientRequest request =
                new AuthenticatedSessionChannelClientRequest("app", null, null, "", "", null);

        assertThat(violationPaths(request)).containsExactlyInAnyOrder("userId", "sessionId", "channelCode");
    }

    @Test
    void shouldAcceptFullyPopulatedCombinedRequest() {
        AuthenticatedSessionChannelClientRequest request =
                new AuthenticatedSessionChannelClientRequest("app", "ios", "1.0.0", "u-1", "s-1", "app-store");

        assertThat(validator.validate(request)).isEmpty();
    }

    private Set<String> violationPaths(Object request) {
        return validator.validate(request).stream()
                .map(violation -> violation.getPropertyPath().toString())
                .collect(Collectors.toSet());
    }
}
