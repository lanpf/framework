package com.cloud.framework.core.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AppRequest implements Request {
    @NotBlank
    private String appId;
    private String appPlatform;
    private String appVersion;

    public AppRequest(String appId, String appPlatform, String appVersion) {
        this.appId = appId;
        this.appPlatform = appPlatform;
        this.appVersion = appVersion;
    }
}
