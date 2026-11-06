package com.cloud.framework.message.support;

import com.cloud.framework.kernel.validation.Require;

public class HashMessageQueueSelector implements MessageQueueSelector {
    @Override
    public Integer select(Integer partitions, Object partitionArg) {
        Require.satisfies(partitions, value -> value > 0, "partitions must be positive");
        Object partitionKey = PartitionKeyResolver.resolve(partitionArg);
        return Math.floorMod(partitionKey.hashCode(), partitions);
    }
}
