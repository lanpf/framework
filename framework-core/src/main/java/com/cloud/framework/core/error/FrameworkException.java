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

    public static FrameworkException missingObjectField() {
        return new FrameworkException(FrameworkError.OBJECT_FILED_REQUIRED);
    }

    public static FrameworkException invalidObjectField() {
        return new FrameworkException(FrameworkError.OBJECT_FIELD_INVALID);
    }

    public static FrameworkException missingCollectionElement() {
        return new FrameworkException(FrameworkError.COLLECTION_ELEMENT_REQUIRED);
    }

    public static FrameworkException invalidCollectionElement() {
        return new FrameworkException(FrameworkError.COLLECTION_ELEMENT_INVALID);
    }
}
