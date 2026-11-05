package com.cloud.framework.core.error;

public class FrameworkException extends BaseException {

    public FrameworkException(Error error, String message) {
        super(error, message);
    }

    public FrameworkException(Error error) {
        super(error);
    }

    public FrameworkException(Error error, String message, Throwable cause) {
        super(error, message, cause);
    }

    public FrameworkException(Error error, Throwable cause) {
        super(error, cause);
    }

    public static FrameworkException illegalArgument(String message) {
        return new FrameworkException(FrameworkError.ILLEGAL_ARGUMENT, message);
    }

    public static FrameworkException illegalArgument() {
        return new FrameworkException(FrameworkError.ILLEGAL_ARGUMENT);
    }

    public static FrameworkException illegalState(String message) {
        return new FrameworkException(FrameworkError.ILLEGAL_STATE, message);
    }

    public static FrameworkException illegalState() {
        return new FrameworkException(FrameworkError.ILLEGAL_STATE);
    }
}
