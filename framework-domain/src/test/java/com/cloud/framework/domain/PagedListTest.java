package com.cloud.framework.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
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
}
