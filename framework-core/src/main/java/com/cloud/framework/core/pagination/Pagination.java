package com.cloud.framework.core.pagination;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;

public interface Pagination {

    int DEFAULT_PAGE_NO = 1;
    int DEFAULT_PAGE_SIZE = 20;
    int MAX_PAGE_SIZE = 200;

    @Positive
    Integer getPageNo();

    @Positive
    @Max(MAX_PAGE_SIZE)
    Integer getPageSize();
}
