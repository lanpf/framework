package com.cloud.framework.core.pagination;

import com.cloud.framework.kernel.validation.Require;

public record PageQuery(int pageNo, int pageSize) {

    private static final PageQuery DEFAULT = new PageQuery(Pagination.DEFAULT_PAGE_NO, Pagination.DEFAULT_PAGE_SIZE);

    public PageQuery {
        Require.satisfies(pageNo, value -> value >= Pagination.DEFAULT_PAGE_NO, "pageNo must be positive");
        Require.satisfies(
                pageSize,
                value -> value >= 1 && value <= Pagination.MAX_PAGE_SIZE,
                "pageSize must be between 1 and " + Pagination.MAX_PAGE_SIZE
        );
    }

    public int zeroBasedPageNo() {
        return pageNo - 1;
    }

    public long offset() {
        return (long) zeroBasedPageNo() * pageSize;
    }

    public static PageQuery of(Integer pageNo, Integer pageSize) {
        return new PageQuery(
                pageNo == null ? Pagination.DEFAULT_PAGE_NO : pageNo,
                pageSize == null ? Pagination.DEFAULT_PAGE_SIZE : pageSize
        );
    }

    public static PageQuery ofSize(Integer pageSize) {
        return of(Pagination.DEFAULT_PAGE_NO, pageSize);
    }

    public static PageQuery from(Pagination pagination) {
        Require.notNull(pagination, "pagination must not be null");
        return of(pagination.getPageNo(), pagination.getPageSize());
    }

    public static PageQuery defaults() {
        return DEFAULT;
    }
}
