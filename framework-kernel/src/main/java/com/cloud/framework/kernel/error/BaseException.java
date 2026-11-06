package com.cloud.framework.kernel.error;

public abstract class BaseException extends RuntimeException {
    private final Error error;
    private final String errorCode;

    protected BaseException(Error error, String message) {
        super(message);
        this.error = requireError(error);
        this.errorCode = this.error.errorCode();
    }

    protected BaseException(Error error) {
        this(error, requireError(error).errorMessage());
    }

    protected BaseException(Error error, String message, Throwable cause) {
        super(message, cause);
        this.error = requireError(error);
        this.errorCode = this.error.errorCode();
    }

    protected BaseException(Error error, Throwable cause) {
        this(error, error.errorMessage(), cause);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Error getError() {
        return error;
    }

    private static Error requireError(Error error) {
        if (error == null) {
            throw FrameworkException.illegalArgument("error must not be null");
        }
        return error;
    }
}
