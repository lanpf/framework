package com.cloud.framework.core.error;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class FrameworkErrorTest {

    @Test
    void shouldFormatCommonErrorCodeWithFixedSixDigits() {
        assertThat(FrameworkError.ILLEGAL_ARGUMENT.errorCode()).isEqualTo("100001");
        assertThat(FrameworkError.ILLEGAL_ARGUMENT.errorMessage()).isEqualTo("FRAMEWORK:Illegal argument");
        assertThat(FrameworkError.ILLEGAL_STATE.errorCode()).isEqualTo("100002");
        assertThat(FrameworkError.ILLEGAL_STATE.errorMessage()).isEqualTo("FRAMEWORK:Illegal state");
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
    void shouldExposeDefaultTechnicalExceptionFactories() {
        assertThat(FrameworkException.illegalArgument())
                .hasFieldOrPropertyWithValue("errorCode", FrameworkError.ILLEGAL_ARGUMENT.errorCode())
                .hasMessage(FrameworkError.ILLEGAL_ARGUMENT.errorMessage());
        assertThat(FrameworkException.illegalState())
                .hasFieldOrPropertyWithValue("errorCode", FrameworkError.ILLEGAL_STATE.errorCode())
                .hasMessage(FrameworkError.ILLEGAL_STATE.errorMessage());
    }

    @Test
    void shouldKeepExplicitTechnicalDiagnosticsWithoutChangingErrorCodes() {
        FrameworkException exception = FrameworkException.illegalArgument("repository must not be null");
        assertThat(exception.getErrorCode()).isEqualTo(FrameworkError.ILLEGAL_ARGUMENT.errorCode());
        assertThat(exception).hasMessage("repository must not be null");
        assertThat(FrameworkException.illegalState("repository is closed"))
                .hasFieldOrPropertyWithValue("errorCode", FrameworkError.ILLEGAL_STATE.errorCode())
                .hasMessage("repository is closed");
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
