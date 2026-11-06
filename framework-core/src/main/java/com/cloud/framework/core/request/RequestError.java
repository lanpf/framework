package com.cloud.framework.core.request;

import com.cloud.framework.kernel.error.BaseError;
import com.cloud.framework.kernel.error.ErrorCodeNamespace;
import com.cloud.framework.kernel.error.ErrorCodePrefix;
import com.cloud.framework.kernel.error.ErrorCodeRange;

public enum RequestError implements BaseError {
    REQUEST_INVALID(20, "Request validation failed"),
    REQUEST_MALFORMED(21, "Request body is malformed"),
    REQUEST_REJECTED(22, "Request was rejected");

    private static final ErrorCodePrefix PREFIX = new ErrorCodePrefix(ErrorCodeNamespace.FRAMEWORK, 0);

    private final int localCode;
    private final String message;

    RequestError(int localCode, String message) {
        ErrorCodeRange.assertLocalCode(localCode);
        this.localCode = localCode;
        this.message = message;
    }

    @Override
    public ErrorCodePrefix getPrefix() {
        return PREFIX;
    }

    @Override
    public int getLocalCode() {
        return localCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
