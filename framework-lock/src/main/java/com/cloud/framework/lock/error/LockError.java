package com.cloud.framework.lock.error;

import com.cloud.framework.kernel.error.BaseError;
import com.cloud.framework.kernel.error.ErrorCodeNamespace;
import com.cloud.framework.kernel.error.ErrorCodePrefix;
import com.cloud.framework.kernel.error.ErrorCodeRange;

public enum LockError implements BaseError {
    CONFIGURATION_INVALID(300, "Lock configuration is invalid");

    private static final ErrorCodePrefix PREFIX = new ErrorCodePrefix(ErrorCodeNamespace.FRAMEWORK, 0);

    private final int localCode;
    private final String message;

    LockError(int localCode, String message) {
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
