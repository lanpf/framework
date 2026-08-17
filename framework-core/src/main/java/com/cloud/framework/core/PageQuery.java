package com.cloud.framework.core;

/**
 * 已完成默认值解析和合法性校验的内部分页查询条件。
 */
public record PageQuery(int pageNo, int pageSize) {

    public static final int DEFAULT_PAGE_NO = PaginationRequest.DEFAULT_PAGE_NO;
    public static final int DEFAULT_PAGE_SIZE = PaginationRequest.DEFAULT_PAGE_SIZE;
    public static final int MAX_PAGE_SIZE = PaginationRequest.MAX_PAGE_SIZE;

    private static final PageQuery DEFAULT = new PageQuery(DEFAULT_PAGE_NO, DEFAULT_PAGE_SIZE);

    public PageQuery {
        if (pageNo < DEFAULT_PAGE_NO) {
            throw new IllegalArgumentException("pageNo must be positive");
        }
        if (pageSize < 1 || pageSize > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException("pageSize must be between 1 and " + MAX_PAGE_SIZE);
        }
    }

    public int zeroBasedPageNo() {
        return pageNo - 1;
    }

    public long offset() {
        return (long) zeroBasedPageNo() * pageSize;
    }

    public static PageQuery of(Integer pageNo, Integer pageSize) {
        return new PageQuery(
                pageNo == null ? DEFAULT_PAGE_NO : pageNo,
                pageSize == null ? DEFAULT_PAGE_SIZE : pageSize
        );
    }

    public static PageQuery ofSize(Integer pageSize) {
        return of(DEFAULT_PAGE_NO, pageSize);
    }

    public static PageQuery from(PaginationRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("pagination request must not be null");
        }
        return of(request.getPageNo(), request.getPageSize());
    }

    public static PageQuery defaults() {
        return DEFAULT;
    }
}
