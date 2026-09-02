package com.cloud.framework.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Test;

class ResultTest {

    @Test
    void shouldCreateSuccessResult() {
        Result<String> result = Result.success("ok");

        assertThat(result).isInstanceOf(BaseResult.class);
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.code()).isEqualTo(BaseResult.DEFAULT_SUCCESS_CODE);
        assertThat(result.message()).isEqualTo(BaseResult.DEFAULT_SUCCESS_MESSAGE);
        assertThat(result.data()).isEqualTo("ok");
        assertThat(result.getCode()).isEqualTo(BaseResult.DEFAULT_SUCCESS_CODE);
        assertThat(result.getMessage()).isEqualTo(BaseResult.DEFAULT_SUCCESS_MESSAGE);
        assertThat(result.getData()).isEqualTo("ok");
    }

    @Test
    void shouldRejectFailureResultWithData() {
        assertThatThrownBy(() -> new Result<>("400001", "failed", "data"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldCreateSuccessPageResult() {
        PageResult<String> result = PageResult.success(List.of("a", "b"), 2L);

        assertThat(result).isInstanceOf(BaseResult.class);
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.data()).containsExactly("a", "b");
        assertThat(result.total()).isEqualTo(2L);
        assertThat(result.getData()).containsExactly("a", "b");
        assertThat(result.getTotal()).isEqualTo(2L);
    }

    @Test
    void shouldCopyPageData() {
        List<String> data = new java.util.ArrayList<>(List.of("a"));

        PageResult<String> result = PageResult.success(data, 1L);
        data.add("b");

        assertThat(result.data()).containsExactly("a");
        assertThatThrownBy(() -> result.data().add("c"))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldRejectFailurePageResultWithData() {
        assertThatThrownBy(() -> new PageResult<>("400001", "failed", List.of("a"), 1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldNormalizeNullSuccessPageDataToEmpty() {
        PageResult<String> result = PageResult.success(null, 0L);

        assertThat(result.data()).isEmpty();
        assertThat(result.total()).isEqualTo(0L);
    }

    @Test
    void shouldKeepNullDataOnFailurePageResult() {
        PageResult<String> result = PageResult.failure("400001", "failed");

        assertThat(result.data()).isNull();
        assertThat(result.isSuccess()).isFalse();
    }
}
