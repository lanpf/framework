package com.cloud.framework.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class RequestHeaderTest {

    @Test
    void shouldDeclareStableHeaderNames() {
        assertThat(RequestHeader.CLIENT_APP_ID).isEqualTo("X-Client-App-Id");
        assertThat(RequestHeader.CLIENT_PLATFORM).isEqualTo("X-Client-Platform");
        assertThat(RequestHeader.CLIENT_VERSION).isEqualTo("X-Client-Version");
        assertThat(RequestHeader.CHANNEL_CODE).isEqualTo("X-Channel-Code");
        assertThat(RequestHeader.USER_ID).isEqualTo("X-User-Id");
        assertThat(RequestHeader.SESSION_ID).isEqualTo("X-Session-Id");
        assertThat(RequestHeader.SUBJECT_TYPE).isEqualTo("X-Subject-Type");
    }
}
