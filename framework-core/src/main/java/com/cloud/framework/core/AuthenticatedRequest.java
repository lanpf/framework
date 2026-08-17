package com.cloud.framework.core;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** 需要消费入口层认证用户与客户端上下文的请求类型。 */
@Getter
@Setter
@NoArgsConstructor
public class AuthenticatedRequest extends ClientRequest {
    @NotNull
    @Positive
    private Long userId;

    public AuthenticatedRequest(
            String clientAppId,
            String clientPlatform,
            String clientVersion,
            Long userId
    ) {
        super(clientAppId, clientPlatform, clientVersion);
        this.userId = userId;
    }
}
