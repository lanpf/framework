package com.cloud.framework.message.support;

import java.util.Map;

public final class PartitionKeyResolver {

    public static Object resolve(Object partitionArg) {
        if (partitionArg instanceof Map<?, ?> map) {
            Object partitionKey = map.get(MessageHeaders.PARTITION_KEY);
            if (partitionKey == null) {
                throw new IllegalArgumentException(MessageHeaders.PARTITION_KEY + " is required");
            }
            return partitionKey;
        }
        if (partitionArg == null) {
            throw new IllegalArgumentException("partition key is required");
        }
        return partitionArg;
    }

    private PartitionKeyResolver() {
    }
}
