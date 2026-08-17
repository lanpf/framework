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
}
