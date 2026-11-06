package com.cloud.framework.core.response;

import com.cloud.framework.kernel.error.BaseException;
import com.cloud.framework.kernel.error.Error;
import com.cloud.framework.kernel.error.FrameworkException;
import com.cloud.framework.core.util.NullSafe;

import java.util.List;

public record PageResult<T>(String code, String message, List<T> data, Long total)
        implements BaseResult {

    /**
     * Creates a response with an immutable shallow copy of successful page data; elements are shared.
     *
     * @param code response code
     * @param message response message
     * @param data may be null: normalized to empty on success, and required to be null on failure;
     *             non-null elements are required
     * @param total total item count, retained as supplied
     */
    public PageResult {
        if (!DEFAULT_SUCCESS_CODE.equals(code) && data != null) {
            throw FrameworkException.illegalArgument("Data must be null if response code is not success");
        }
        data = DEFAULT_SUCCESS_CODE.equals(code) ? NullSafe.copyToList(data) : null;
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
