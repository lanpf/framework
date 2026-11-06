package com.cloud.framework.message.support;

import com.cloud.framework.kernel.error.FrameworkException;
import com.cloud.framework.message.error.MessageError;

import java.util.Map;

public final class PartitionKeyResolver {

    public static Object resolve(Object partitionArg) {
        if (partitionArg instanceof Map<?, ?> map) {
            Object partitionKey = map.get(MessageHeaders.PARTITION_KEY);
            if (partitionKey == null) {
                throw new FrameworkException(
                        MessageError.PARTITION_KEY_MISSING,
                        MessageHeaders.PARTITION_KEY + " is required"
                );
            }
            return partitionKey;
        }
        if (partitionArg == null) {
            throw new FrameworkException(MessageError.PARTITION_KEY_MISSING, "partition key is required");
        }
        return partitionArg;
    }

    private PartitionKeyResolver() {
    }
}
