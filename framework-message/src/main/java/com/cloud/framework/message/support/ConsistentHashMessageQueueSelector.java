package com.cloud.framework.message.support;

import com.google.common.hash.Hashing;
import org.apache.commons.lang3.Validate;

import java.nio.charset.StandardCharsets;

public class ConsistentHashMessageQueueSelector implements MessageQueueSelector {
    @Override
    public Integer select(Integer partitions, Object partitionArg) {
        Validate.notNull(partitions, "partitions must not be null");
        Validate.isTrue(partitions > 0, "partitions must be positive");
        Object partitionKey = PartitionKeyResolver.resolve(partitionArg);
        long hashCode = Hashing.murmur3_128()
                .hashString(partitionKey.toString(), StandardCharsets.UTF_8)
                .asLong();
        return Hashing.consistentHash(hashCode, partitions);
    }
}
