package com.cloud.framework.core.error;

import org.apache.commons.lang3.Validate;

import java.io.Serializable;

public interface ErrorCodeRange extends Serializable {
    int CODE_DIGITS = 6;
    int LOCAL_CODE_DIGITS = CODE_DIGITS / 2;
    int LOCAL_CODE_BASE = (int) Math.pow(10, LOCAL_CODE_DIGITS);
    int LOCAL_CODE_MIN = 0;
    int LOCAL_CODE_MAX = LOCAL_CODE_BASE - 1;

    default int min() {
        return getMin();
    }

    default int max() {
        return getMax();
    }

    int getMin();

    int getMax();

    default void assertContains(int code) {
        Validate.isTrue(
                code >= getMin() && code <= getMax(),
                String.format("%d out of range [%d, %d]", code, getMin(), getMax())
        );
    }

    default String format(int code) {
        assertContains(code);
        return String.format("%0" + CODE_DIGITS + "d", code);
    }

    static void assertLocalCode(int localCode) {
        Validate.isTrue(
                localCode >= LOCAL_CODE_MIN && localCode <= LOCAL_CODE_MAX,
                String.format("%d out of local code range [%d, %d]", localCode, LOCAL_CODE_MIN, LOCAL_CODE_MAX)
        );
    }

}
