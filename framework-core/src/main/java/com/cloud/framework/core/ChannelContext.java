package com.cloud.framework.core;

import jakarta.validation.constraints.NotBlank;

/** 需要消费入口层可信渠道上下文的请求能力。 */
public interface ChannelContext {

    @NotBlank
    String getChannelCode();

    void setChannelCode(String channelCode);
}
