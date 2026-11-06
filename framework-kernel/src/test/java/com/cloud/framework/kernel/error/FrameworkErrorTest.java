package com.cloud.framework.kernel.error;

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
        assertThat(exception.getError()).isSameAs(FrameworkError.ILLEGAL_ARGUMENT);
        assertThat(exception).hasMessage("repository must not be null");
        assertThat(FrameworkException.illegalState("repository is closed"))
                .hasFieldOrPropertyWithValue("errorCode", FrameworkError.ILLEGAL_STATE.errorCode())
                .hasMessage("repository is closed");
    }

    @Test
    void shouldRejectMissingErrorWithoutThrowingNullPointerException() {
        assertThatThrownBy(() -> new FrameworkException((Error) null))
                .isInstanceOf(FrameworkException.class)
                .hasFieldOrPropertyWithValue("errorCode", FrameworkError.ILLEGAL_ARGUMENT.errorCode())
                .hasMessage("error must not be null");
    }

    @Test
    void shouldExposeNonOverlappingNamespaceRanges() {
        assertThat(ErrorCodeNamespace.FRAMEWORK.min()).isEqualTo(100000);
        assertThat(ErrorCodeNamespace.FRAMEWORK.max()).isEqualTo(199999);
        assertThat(ErrorCodeNamespace.EDGE.min()).isEqualTo(200000);
        assertThat(ErrorCodeNamespace.EDGE.max()).isEqualTo(299999);
        assertThat(ErrorCodeNamespace.BIZ.min()).isEqualTo(300000);
        assertThat(ErrorCodeNamespace.BIZ.max()).isEqualTo(899999);
    }

    @Test
    void shouldRejectInvalidLocalCode() {
        assertThatThrownBy(() -> ErrorCodeRange.assertLocalCode(-1))
                .isInstanceOf(FrameworkException.class)
                .hasFieldOrPropertyWithValue("errorCode", FrameworkError.ILLEGAL_ARGUMENT.errorCode());
        assertThatThrownBy(() -> ErrorCodeRange.assertLocalCode(1000))
                .isInstanceOf(FrameworkException.class)
                .hasFieldOrPropertyWithValue("errorCode", FrameworkError.ILLEGAL_ARGUMENT.errorCode());
    }
}
