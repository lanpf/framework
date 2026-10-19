package com.cloud.framework.core.request;

import jakarta.validation.constraints.NotBlank;

public interface AuthenticatedSessionContext {

    @NotBlank
    String getUserId();

    void setUserId(String userId);

    @NotBlank
    String getSessionId();

    void setSessionId(String sessionId);
}
