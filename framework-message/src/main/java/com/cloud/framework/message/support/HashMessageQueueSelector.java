package com.cloud.framework.message.support;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class HashMessageQueueSelector implements MessageQueueSelector {
    @Override
    public Integer select(@NotNull @Positive Integer partitions, @NotNull Object partitionArg) {
        Object partitionKey = MessageHeaders.resolvePartitionKey(partitionArg);
        return Math.floorMod(partitionKey.hashCode(), partitions);
    }
}
