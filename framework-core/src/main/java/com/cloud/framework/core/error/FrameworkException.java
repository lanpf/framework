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

    public static FrameworkException unspecified() {
        return new FrameworkException(FrameworkError.FRAMEWORK_ERROR);
    }

    public static FrameworkException missingObject() {
        return new FrameworkException(FrameworkError.OBJECT_REQUIRED);
    }

    public static FrameworkException invalidObject() {
        return new FrameworkException(FrameworkError.OBJECT_INVALID);
    }
}
