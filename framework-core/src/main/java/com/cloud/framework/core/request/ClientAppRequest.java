package com.cloud.framework.core.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClientAppRequest extends AppRequest implements ClientContext {
    @NotBlank
    private String clientId;

    public ClientAppRequest(
            String appId,
            String appPlatform,
            String appVersion,
            String clientId
    ) {
        super(appId, appPlatform, appVersion);
        this.clientId = clientId;
    }
}
