package com.cloud.framework.core;

/** 受入口层写入的通用请求上下文 Header 名称。 */
public final class RequestHeader {
    public static final String CLIENT_APP_ID = "X-Client-App-Id";
    public static final String CLIENT_PLATFORM = "X-Client-Platform";
    public static final String CLIENT_VERSION = "X-Client-Version";
    public static final String CHANNEL_CODE = "X-Channel-Code";
    public static final String USER_ID = "X-User-Id";

    private RequestHeader() {
    }
}
