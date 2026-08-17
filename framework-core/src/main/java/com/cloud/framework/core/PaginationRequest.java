package com.cloud.framework.core;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;

/**
 * 可与客户端、渠道及业务查询条件组合的分页请求能力。
 */
public interface PaginationRequest extends Request {

    int DEFAULT_PAGE_NO = 1;
    int DEFAULT_PAGE_SIZE = 20;
    int MAX_PAGE_SIZE = 200;

    @Positive
    Integer getPageNo();

    @Positive
    @Max(MAX_PAGE_SIZE)
    Integer getPageSize();
}
