package com.cloud.framework.core.pagination;

import com.cloud.framework.core.util.NullSafe;

import java.io.Serializable;
import java.util.List;

/** Technology-neutral page data with an immutable list container and shared element references. */
public record PagedList<T>(List<T> data, Long total) implements Serializable {

    /**
     * Creates page data with an immutable shallow list copy.
     *
     * @param data may be null, equivalent to an empty list; null elements are rejected
     * @param total total item count, retained as supplied
     */
    public PagedList {
        data = NullSafe.copyToList(data);
    }

    public List<T> getData() {
        return data;
    }

    public Long getTotal() {
        return total;
    }

    public static <T> PagedList<T> empty() {
        return new PagedList<>(List.of(), 0L);
    }
}
