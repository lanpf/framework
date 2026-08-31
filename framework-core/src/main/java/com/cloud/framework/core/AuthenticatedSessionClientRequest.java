package com.cloud.framework.core;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** 需要消费入口层认证用户与客户端上下文的请求类型。 */
@Getter
@Setter
@NoArgsConstructor
public class AuthenticatedSessionClientRequest extends ClientRequest implements AuthenticatedSessionContext {

    @NotBlank
    private String userId;

    @NotBlank
    private String sessionId;

    public AuthenticatedSessionClientRequest(
            String clientAppId,
            String clientPlatform,
            String clientVersion,
            String userId,
            String sessionId
    ) {
        super(clientAppId, clientPlatform, clientVersion);
        this.userId = userId;
        this.sessionId = sessionId;
    }
}
