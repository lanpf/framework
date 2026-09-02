package com.cloud.framework.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SubjectTypeTest {

    @Test
    void shouldDistinguishHostAndBrowserSessions() {
        assertThat(SubjectType.values())
                .extracting(Enum::name)
                .containsExactly("HOST_SESSION", "BROWSER_SESSION");
    }
}
