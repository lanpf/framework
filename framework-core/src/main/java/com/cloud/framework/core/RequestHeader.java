package com.cloud.framework.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/** 已登记的 HTTP Header 契约；分类不代表请求传入值可信。 */
@Getter
@RequiredArgsConstructor
public enum RequestHeader {
    APP_ID("X-App-Id", HeaderType.CONTEXT),
    APP_PLATFORM("X-App-Platform", HeaderType.CONTEXT),
    APP_VERSION("X-App-Version", HeaderType.CONTEXT),
    CLIENT_ID("X-Client-Id", HeaderType.CONTEXT),
    USER_ID("X-User-Id", HeaderType.CONTEXT),
    SESSION_ID("X-Session-Id", HeaderType.CONTEXT),
    SUBJECT_TYPE("X-Subject-Type", HeaderType.CONTEXT),

    SIGNATURE("X-Signature", HeaderType.SIGNATURE),
    TIMESTAMP("X-Timestamp", HeaderType.SIGNATURE),
    NONCE("X-Nonce", HeaderType.SIGNATURE),
    CREDENTIAL_ID("X-Credential-Id", HeaderType.SIGNATURE);

    private final String headerName;
    private final HeaderType headerType;
    private static final Map<String, RequestHeader> BY_HEADER_NAME = index();

    public String headerName() { return getHeaderName(); }
    public HeaderType headerType() { return getHeaderType(); }

    public static Optional<RequestHeader> fromName(String headerName) {
        Objects.requireNonNull(headerName, "Header name must not be null");
        return Optional.ofNullable(BY_HEADER_NAME.get(normalize(headerName)));
    }

    public static Set<RequestHeader> fromType(HeaderType headerType) {
        Objects.requireNonNull(headerType, "Header headerType must not be null");
        return Arrays.stream(values()).filter(header -> header.headerType == headerType)
                .collect(Collectors.toUnmodifiableSet());
    }

    private static String normalize(String name) {
        return name.toLowerCase(Locale.ROOT);
    }

    private static Map<String, RequestHeader> index() {
        Map<String, RequestHeader> index = new HashMap<>();
        for (RequestHeader header : values()) {
            if (index.putIfAbsent(normalize(header.headerName), header) != null) {
                throw new IllegalStateException("Duplicate normalized header name");
            }
        }
        return Map.copyOf(index);
    }
}
