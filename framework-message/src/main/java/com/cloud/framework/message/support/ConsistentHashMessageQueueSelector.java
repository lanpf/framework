package com.cloud.framework.message.support;

import com.google.common.hash.Hashing;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.nio.charset.StandardCharsets;

public class ConsistentHashMessageQueueSelector implements MessageQueueSelector {
    @Override
    public Integer select(@NotNull @Positive Integer partitions, @NotNull Object partitionArg) {
        Object partitionKey = MessageHeaders.resolvePartitionKey(partitionArg);
        long hashCode = Hashing.murmur3_128()
                .hashString(partitionKey.toString(), StandardCharsets.UTF_8)
                .asLong();
        return Hashing.consistentHash(hashCode, partitions);
    }
}
