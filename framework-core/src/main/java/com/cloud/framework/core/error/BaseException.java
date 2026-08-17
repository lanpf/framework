package com.cloud.framework.core.error;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {
    private final String errorCode;

    protected BaseException(Error error, String message) {
        super(message);
        this.errorCode = error.errorCode();
    }

    protected BaseException(Error error) {
        this(error, error.errorMessage());
    }

    protected BaseException(Error error, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = error.errorCode();
    }

    protected BaseException(Error error, Throwable cause) {
        this(error, error.errorMessage(), cause);
    }
}
