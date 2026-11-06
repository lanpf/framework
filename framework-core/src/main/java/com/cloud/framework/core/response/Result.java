package com.cloud.framework.core.response;

import com.cloud.framework.kernel.error.BaseException;
import com.cloud.framework.kernel.error.Error;
import com.cloud.framework.kernel.error.FrameworkException;

/**
 * API response envelope retaining the original payload reference without taking ownership of its state.
 * No payload copy, snapshot, or deep immutability is promised. Callers provide any isolation and
 * publication stability required by the payload's own contract; arbitrary payloads need not be immutable.
 *
 * @param <T> payload type, preserved without runtime-type conversion
 */
public record Result<T>(String code, String message, T data) implements BaseResult {

    public Result {
        if (!DEFAULT_SUCCESS_CODE.equals(code) && data != null) {
            throw FrameworkException.illegalArgument("Data must be null if response code is not success");
        }
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
