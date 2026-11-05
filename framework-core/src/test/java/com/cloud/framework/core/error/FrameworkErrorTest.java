package com.cloud.framework.core.error;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class FrameworkErrorTest {

    @Test
    void shouldFormatCommonErrorCodeWithFixedSixDigits() {
        assertThat(FrameworkError.FRAMEWORK_ERROR.errorCode()).isEqualTo("100000");
        assertThat(FrameworkError.FRAMEWORK_ERROR.errorMessage()).isEqualTo("FRAMEWORK:Framework error");
    }

    @Test
    void shouldExposeEnglishDefaultMessagesForEveryFrameworkError() {
        for (FrameworkError error : FrameworkError.values()) {
            assertThat(error.message()).as(error.name()).matches("(?=.*[A-Za-z])[\\x20-\\x7E]+");
            assertThat(error.errorMessage()).isEqualTo("FRAMEWORK:" + error.message());
            FrameworkException exception = new FrameworkException(error);
            assertThat(exception.getErrorCode()).isEqualTo(error.errorCode());
            assertThat(exception).hasMessage(error.errorMessage());
        }
    }

    @Test
    void shouldKeepExplicitTechnicalDiagnosticsWithoutChangingErrorCodes() {
        assertThat(FrameworkException.unspecifiedError())
                .hasMessage(FrameworkError.FRAMEWORK_ERROR.errorMessage());
        assertThat(FrameworkException.requiredError())
                .hasMessage(FrameworkError.OBJECT_REQUIRED.errorMessage());
        assertThat(FrameworkException.invalidError())
                .hasMessage(FrameworkError.OBJECT_INVALID.errorMessage());

        FrameworkException exception = FrameworkException.requiredError("repository must not be null");
        assertThat(exception.getErrorCode()).isEqualTo(FrameworkError.OBJECT_REQUIRED.errorCode());
        assertThat(exception).hasMessage("repository must not be null");
    }

    @Test
    void shouldExposeNamespaceAndPrefixRanges() {
        assertThat(ErrorCodeNamespace.BIZ.min()).isEqualTo(200000);
        assertThat(ErrorCodeNamespace.BIZ.max()).isEqualTo(799999);
    }

    @Test
    void shouldRejectInvalidLocalCode() {
        assertThatThrownBy(() -> ErrorCodeRange.assertLocalCode(-1))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> ErrorCodeRange.assertLocalCode(1000))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
