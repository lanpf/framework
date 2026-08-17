package com.cloud.framework.core.error;

public interface BaseError extends Error {

    String name();

    default ErrorCodePrefix prefix() {
        return getPrefix();
    }

    default int localCode() {
        return getLocalCode();
    }

    default String message() {
        return getMessage();
    }

    default int code() {
        return getCode();
    }

    ErrorCodePrefix getPrefix();

    int getLocalCode();

    String getMessage();

    default int getCode() {
        ErrorCodeRange.assertLocalCode(getLocalCode());
        return getPrefix().getMin() + getLocalCode();
    }

    @Override
    default String getErrorCode() {
        return getPrefix().format(getCode());
    }

    @Override
    default String getErrorMessage() {
        String configuredMessage = getMessage();
        String resolvedMessage = configuredMessage != null && !configuredMessage.trim().isEmpty()
                ? configuredMessage
                : name().replace('_', ' ');
        return getPrefix().getNamespace().name() + ":" + resolvedMessage;
    }
}
