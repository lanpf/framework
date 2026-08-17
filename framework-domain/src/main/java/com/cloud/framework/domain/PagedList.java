package com.cloud.framework.domain;

import java.io.Serializable;
import java.util.List;

/** 应用与领域内部使用的不可变分页数据。 */
public record PagedList<T>(List<T> data, Long total) implements Serializable {

    public PagedList {
        data = data == null ? List.of() : List.copyOf(data);
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
