package com.cloud.framework.core.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthenticatedSessionAppRequest extends AppRequest implements AuthenticatedSessionContext {

    @NotBlank
    private String userId;

    @NotBlank
    private String sessionId;

    public AuthenticatedSessionAppRequest(
            String appId,
            String appPlatform,
            String appVersion,
            String userId,
            String sessionId
    ) {
        super(appId, appPlatform, appVersion);
        this.userId = userId;
        this.sessionId = sessionId;
    }
}
