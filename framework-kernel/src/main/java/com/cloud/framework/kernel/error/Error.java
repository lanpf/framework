package com.cloud.framework.kernel.error;

import java.io.Serializable;

public interface Error extends Serializable {
    default String errorCode() {
        return getErrorCode();
    }

    default String errorMessage() {
        return getErrorMessage();
    }

    String getErrorCode();

    String getErrorMessage();
}
