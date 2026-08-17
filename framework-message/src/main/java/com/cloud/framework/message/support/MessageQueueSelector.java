package com.cloud.framework.message.support;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public interface MessageQueueSelector {
    Integer select(@NotNull @Positive Integer partitions, @NotNull Object partitionArg);
}
