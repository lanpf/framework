package com.cloud.framework.core;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 需要消费入口层客户端与渠道上下文的请求类型。
 *
 * <p>既可直接作为只需要客户端与渠道上下文的请求体，也可作为带有业务字段的请求类型的父类。
 * Web MVC starter 会使用受保护 HTTP Header 覆盖这些字段，调用方不应信任请求体中的同名值。</p>
 */
@Getter
@Setter
@NoArgsConstructor
public class ClientChannelRequest extends ClientRequest {
    @NotBlank
    private String channelCode;

    public ClientChannelRequest(
            String clientAppId,
            String clientPlatform,
            String clientVersion,
            String channelCode
    ) {
        super(clientAppId, clientPlatform, clientVersion);
        this.channelCode = channelCode;
    }
}
