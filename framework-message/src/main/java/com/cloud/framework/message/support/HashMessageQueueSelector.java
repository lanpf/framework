package com.cloud.framework.message.support;

import org.apache.commons.lang3.Validate;

public class HashMessageQueueSelector implements MessageQueueSelector {
    @Override
    public Integer select(Integer partitions, Object partitionArg) {
        Validate.notNull(partitions, "partitions must not be null");
        Validate.isTrue(partitions > 0, "partitions must be positive");
        Object partitionKey = PartitionKeyResolver.resolve(partitionArg);
        return Math.floorMod(partitionKey.hashCode(), partitions);
    }
}
