package com.cloud.framework.core;

import java.io.Serializable;
import java.util.Objects;

public interface BaseResult extends Serializable {
    String DEFAULT_SUCCESS_CODE = "0";
    String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";

    String getCode();

    String getMessage();

    default boolean isSuccess() {
        return Objects.equals(DEFAULT_SUCCESS_CODE, getCode());
    }
}
