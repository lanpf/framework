package com.cloud.framework.message.error;

import com.cloud.framework.kernel.error.BaseError;
import com.cloud.framework.kernel.error.ErrorCodeNamespace;
import com.cloud.framework.kernel.error.ErrorCodePrefix;
import com.cloud.framework.kernel.error.ErrorCodeRange;

public enum MessageError implements BaseError {
    PUBLISHER_CONFIRM_REQUIRED(400, "Publisher confirm mode is required"),
    PUBLISHER_CONFIRM_INTERRUPTED(401, "Publisher confirm wait was interrupted"),
    PUBLISHER_CONFIRM_TIMEOUT(402, "Publisher confirm timed out"),
    PUBLISHER_CONFIRM_FAILED(403, "Publisher confirm failed"),
    MESSAGE_RETURNED(404, "Message was returned by the broker"),
    PARTITION_KEY_MISSING(405, "Message partition key is missing"),
    CONFIGURATION_INVALID(406, "Message configuration is invalid"),
    DESTINATION_NOT_CONFIGURED(407, "Message destination is not configured"),
    DELAY_INVALID(408, "Message delay is invalid"),
    DELAY_OUT_OF_RANGE(409, "Message delay exceeds the supported range");

    private static final ErrorCodePrefix PREFIX = new ErrorCodePrefix(ErrorCodeNamespace.FRAMEWORK, 0);

    private final int localCode;
    private final String message;

    MessageError(int localCode, String message) {
        ErrorCodeRange.assertLocalCode(localCode);
        this.localCode = localCode;
        this.message = message;
    }

    @Override
    public ErrorCodePrefix getPrefix() {
        return PREFIX;
    }

    @Override
    public int getLocalCode() {
        return localCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
