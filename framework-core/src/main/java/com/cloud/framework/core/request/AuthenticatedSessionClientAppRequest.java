package com.cloud.framework.core.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthenticatedSessionClientAppRequest extends AuthenticatedSessionAppRequest implements ClientContext {

    @NotBlank
    private String clientId;

    public AuthenticatedSessionClientAppRequest(
            String appId,
            String appPlatform,
            String appVersion,
            String userId,
            String sessionId,
            String clientId
    ) {
        super(appId, appPlatform, appVersion, userId, sessionId);
        this.clientId = clientId;
    }
}
