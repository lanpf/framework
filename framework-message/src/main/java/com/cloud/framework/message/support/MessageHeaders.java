package com.cloud.framework.message.support;

import java.util.Map;

public final class MessageHeaders {
    private MessageHeaders() {
    }
    public static final String PREFIX = "x-";
    public static final String PARTITION_KEY = PREFIX + "partition-key";


    public static Object resolvePartitionKey(Object partitionArg) {
        if (partitionArg instanceof Map<?, ?> map) {
            Object partitionKey = map.get(PARTITION_KEY);
            if (partitionKey == null) {
                throw new IllegalArgumentException(PARTITION_KEY + " is required");
            }
            return partitionKey;
        }
        if (partitionArg == null) {
            throw new IllegalArgumentException("partition key is required");
        }
        return partitionArg;
    }
}
