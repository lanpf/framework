package com.cloud.framework.core;

import com.cloud.framework.core.error.FrameworkException;
import com.cloud.framework.core.validation.Require;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

    private static final int MAX_NAME_LENGTH = 128;
    private static final String NAME_TOKEN = "[!#$%&'*+.^_`|~0-9A-Za-z-]+";

    private static final Map<String, RequestHeader> INDEXED
            = Require.noDuplicates(List.of(values()), v -> normalizeName(v.headerName));

    public String headerName() { return getHeaderName(); }
    public HeaderType headerType() { return getHeaderType(); }

    public static Optional<RequestHeader> fromName(String headerName) {
        String normalizedName = normalizeName(headerName);
        return normalizedName == null
                ? Optional.empty()
                : Optional.ofNullable(INDEXED.get(normalizedName));
    }

    public static Set<RequestHeader> fromType(HeaderType headerType) {
        return headerType == null
                ? Set.of()
                : INDEXED.values().stream().filter(v -> v.headerType == headerType).collect(Collectors.toUnmodifiableSet());
    }

    public static String normalizeName(String headerName) {
        if (headerName == null) {
            return null;
        }
        if (headerName.length() <= MAX_NAME_LENGTH && headerName.matches(NAME_TOKEN)) {
            return headerName.toLowerCase(Locale.ROOT);
        }
        throw FrameworkException.invalidObject();
    }
}
