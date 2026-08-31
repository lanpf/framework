package com.cloud.framework.core;

import jakarta.validation.constraints.NotBlank;

/** 需要消费入口层可信认证会话上下文的请求能力。 */
public interface AuthenticatedSessionContext {

    @NotBlank
    String getUserId();

    void setUserId(String userId);

    @NotBlank
    String getSessionId();

    void setSessionId(String sessionId);
}
