package com.cloud.framework.core.error;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class FrameworkErrorTest {

    @Test
    void shouldFormatCommonErrorCodeWithFixedSixDigits() {
        assertThat(FrameworkError.FRAMEWORK_ERROR.errorCode()).isEqualTo("100000");
        assertThat(FrameworkError.FRAMEWORK_ERROR.errorMessage()).isEqualTo("FRAMEWORK:框架异常");
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
