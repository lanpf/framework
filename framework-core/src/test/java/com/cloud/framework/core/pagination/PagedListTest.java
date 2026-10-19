package com.cloud.framework.core.pagination;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.cloud.framework.core.error.FrameworkError;
import com.cloud.framework.core.error.FrameworkException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class PagedListTest {

    @Test
    void shouldNormalizeNullDataToEmpty() {
        PagedList<String> pagedList = new PagedList<>(null, 5L);

        assertThat(pagedList.data()).isEmpty();
        assertThat(pagedList.getData()).isEmpty();
        assertThat(pagedList.total()).isEqualTo(5L);
    }

    @Test
    void shouldCopyDataDefensively() {
        List<String> source = new ArrayList<>(List.of("a"));

        PagedList<String> pagedList = new PagedList<>(source, 1L);
        source.add("b");

        assertThat(pagedList.data()).containsExactly("a");
        assertThat(pagedList.data()).isUnmodifiable();
    }

    @Test
    void shouldCreateEmptyPageList() {
        assertThat(PagedList.empty().data()).isEmpty();
        assertThat(PagedList.empty().total()).isZero();
    }

    @Test
    void shouldRejectNullElementsWithFrameworkError() {
        assertThatThrownBy(() -> new PagedList<>(Arrays.asList("a", null), 2L))
                .isInstanceOf(FrameworkException.class)
                .hasFieldOrPropertyWithValue("errorCode", FrameworkError.OBJECT_INVALID.errorCode());
    }

    @Test
    void shouldCopyContainerWithoutCopyingElements() {
        List<String> element = new ArrayList<>(List.of("a"));
        List<List<String>> source = new ArrayList<>(List.of(element));
        PagedList<List<String>> page = new PagedList<>(source, 1L);
        source.clear();
        element.add("b");

        assertThat(page.data()).hasSize(1);
        assertThat(page.data().get(0)).isSameAs(element).containsExactly("a", "b");
        assertThat(page.data()).isUnmodifiable();
    }
}
