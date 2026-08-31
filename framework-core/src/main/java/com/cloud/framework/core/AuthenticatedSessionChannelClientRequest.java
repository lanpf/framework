package com.cloud.framework.core;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** 同时消费客户端、渠道与认证会话上下文的请求类型。 */
@Getter
@Setter
@NoArgsConstructor
public class AuthenticatedSessionChannelClientRequest extends AuthenticatedSessionClientRequest implements ChannelContext {

    @NotBlank
    private String channelCode;

    public AuthenticatedSessionChannelClientRequest(
            String clientAppId,
            String clientPlatform,
            String clientVersion,
            String userId,
            String sessionId,
            String channelCode
    ) {
        super(clientAppId, clientPlatform, clientVersion, userId, sessionId);
        this.channelCode = channelCode;
    }
}
