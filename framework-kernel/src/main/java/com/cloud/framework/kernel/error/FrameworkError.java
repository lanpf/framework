package com.cloud.framework.kernel.error;

public enum FrameworkError implements BaseError {
    INTERNAL_ERROR(0, "Internal error"),
    ILLEGAL_ARGUMENT(1, "Illegal argument"),
    ILLEGAL_STATE(2, "Illegal state");

    private static final ErrorCodePrefix PREFIX = new ErrorCodePrefix(ErrorCodeNamespace.FRAMEWORK, 0);

    private final int localCode;
    private final String message;

    FrameworkError(int localCode, String message) {
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
