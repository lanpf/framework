package com.cloud.framework.core;

import java.util.Locale;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class RequestHeaderTest {
    @Test void shouldResolveAllWireNamesWithoutCaseSensitivity() {
        for (RequestHeader header : RequestHeader.values()) {
            assertThat(RequestHeader.fromName(header.headerName())).contains(header);
            assertThat(RequestHeader.fromName(header.headerName().toUpperCase(Locale.ROOT))).contains(header);
            assertThat(RequestHeader.fromName(header.headerName().toLowerCase(Locale.ROOT))).contains(header);
            assertThat(header.getHeaderName()).isEqualTo(header.headerName());
            assertThat(header.getHeaderType()).isEqualTo(header.headerType());
        }
    }

    @Test void shouldDeriveImmutableCategories() {
        assertThat(RequestHeader.fromType(HeaderType.SIGNATURE)).containsExactlyInAnyOrder(
                RequestHeader.SIGNATURE, RequestHeader.TIMESTAMP, RequestHeader.NONCE, RequestHeader.CREDENTIAL_ID);
        assertThatThrownBy(() -> RequestHeader.fromType(HeaderType.CONTEXT).clear())
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test void shouldNormalizeIndependentlyOfDefaultLocale() {
        Locale previous = Locale.getDefault();
        try {
            Locale.setDefault(Locale.forLanguageTag("tr-TR"));
            assertThat(RequestHeader.fromName("X-CLIENT-ID")).contains(RequestHeader.CLIENT_ID);
        } finally {
            Locale.setDefault(previous);
        }
    }
}
