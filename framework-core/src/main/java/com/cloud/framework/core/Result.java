package com.cloud.framework.core;

import com.cloud.framework.core.error.BaseException;
import com.cloud.framework.core.error.Error;
import org.apache.commons.lang3.Validate;

public record Result<T>(String code, String message, T data) implements BaseResult {

    public Result {
        Validate.isTrue(
                DEFAULT_SUCCESS_CODE.equals(code) || data == null,
                "Data must be null if response code is not success"
        );
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public static Result<Void> success() {
        return success(null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(DEFAULT_SUCCESS_CODE, DEFAULT_SUCCESS_MESSAGE, data);
    }

    public static <T> Result<T> failure(String code, String message) {
        return new Result<>(code, message, null);
    }

    public static <T> Result<T> failure(Error error) {
        return failure(error.errorCode(), error.errorMessage());
    }

    public static <T> Result<T> failure(BaseException exception) {
        return failure(exception.getErrorCode(), exception.getMessage());
    }
}
