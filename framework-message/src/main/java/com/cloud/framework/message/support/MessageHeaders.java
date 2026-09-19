package com.cloud.framework.message.support;

public final class MessageHeaders {
    private MessageHeaders() {
    }
    public static final String PREFIX = "x-";
    public static final String PARTITION_KEY = PREFIX + "partition-key";
}
