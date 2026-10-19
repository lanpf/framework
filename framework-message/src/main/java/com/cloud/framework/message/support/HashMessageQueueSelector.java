package com.cloud.framework.message.support;

import com.google.common.base.Preconditions;

import java.util.Objects;

public class HashMessageQueueSelector implements MessageQueueSelector {
    @Override
    public Integer select(Integer partitions, Object partitionArg) {
        Objects.requireNonNull(partitions, "partitions must not be null");
        Preconditions.checkArgument(partitions > 0, "partitions must be positive");
        Object partitionKey = PartitionKeyResolver.resolve(partitionArg);
        return Math.floorMod(partitionKey.hashCode(), partitions);
    }
}
