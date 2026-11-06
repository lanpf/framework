package com.cloud.framework.core.response;

import com.cloud.framework.kernel.error.FrameworkError;
import com.cloud.framework.kernel.error.FrameworkException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ResponsePayloadContractTest {

    @Test
    void shouldPreserveConcretePayloadTypeAndReference() {
        ArrayList<String> payload = new ArrayList<>(List.of("a"));
        Result<ArrayList<String>> result = Result.success(payload);
        payload.add("b");

        assertThat(result.data()).isSameAs(payload).containsExactly("a", "b");
        assertThat(result.getData()).isSameAs(payload);
        assertThat(Result.success().data()).isNull();
    }

    @Test
    void shouldRejectNullPageElementsUsingFrameworkError() {
        assertThatThrownBy(() -> PageResult.success(Arrays.asList("a", null), 2L))
                .isInstanceOf(FrameworkException.class)
                .hasFieldOrPropertyWithValue("errorCode", FrameworkError.ILLEGAL_ARGUMENT.errorCode());
    }

    @Test
    void shouldKeepDifferentSuccessAndFailureNullSemantics() {
        assertThat(PageResult.success(null, 0L).data()).isEmpty();
        assertThat(PageResult.failure("400001", "failed").data()).isNull();
        assertThatThrownBy(() -> new PageResult<>("400001", "failed", List.of(), 0L))
                .isInstanceOf(FrameworkException.class)
                .hasFieldOrPropertyWithValue("errorCode", FrameworkError.ILLEGAL_ARGUMENT.errorCode());
    }
}
