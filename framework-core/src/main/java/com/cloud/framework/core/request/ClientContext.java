package com.cloud.framework.core.request;

import jakarta.validation.constraints.NotBlank;

public interface ClientContext {

    @NotBlank
    String getClientId();

    void setClientId(String clientId);
}
