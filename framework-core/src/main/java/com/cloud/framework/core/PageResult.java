package com.cloud.framework.core;

import com.cloud.framework.core.error.BaseException;
import com.cloud.framework.core.error.Error;
import java.util.List;

public record PageResult<T>(String code, String message, List<T> data, Long total)
        implements BaseResult {

    public PageResult {
        if (!DEFAULT_SUCCESS_CODE.equals(code) && data != null) {
            throw new IllegalArgumentException("Data must be null if response code is not success");
        }
        data = data == null ? null : List.copyOf(data);
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public List<T> getData() {
        return data;
    }

    public Long getTotal() {
        return total;
    }

    public static <T> PageResult<T> empty() {
        return success(List.of(), 0L);
    }

    public static <T> PageResult<T> success(List<T> data, Long total) {
        return new PageResult<>(DEFAULT_SUCCESS_CODE, DEFAULT_SUCCESS_MESSAGE, data, total);
    }

    public static <T> PageResult<T> failure(String code, String message) {
        return new PageResult<>(code, message, null, null);
    }

    public static <T> PageResult<T> failure(Error error) {
        return failure(error.errorCode(), error.errorMessage());
    }

    public static <T> PageResult<T> failure(BaseException exception) {
        return failure(exception.getErrorCode(), exception.getMessage());
    }
}
