package com.cloud.framework.core;

import org.apache.commons.lang3.Validate;

/**
 * 已完成默认值解析和合法性校验的内部分页查询条件。
 */
public record PageQuery(int pageNo, int pageSize) {

    private static final PageQuery DEFAULT = new PageQuery(Pagination.DEFAULT_PAGE_NO, Pagination.DEFAULT_PAGE_SIZE);

    public PageQuery {
        Validate.isTrue(pageNo >= Pagination.DEFAULT_PAGE_NO, "pageNo must be positive");
        Validate.isTrue(
                pageSize >= 1 && pageSize <= Pagination.MAX_PAGE_SIZE,
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
        Validate.notNull(pagination, "pagination must not be null");
        return of(pagination.getPageNo(), pagination.getPageSize());
    }

    public static PageQuery defaults() {
        return DEFAULT;
    }
}
