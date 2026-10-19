package com.cloud.framework.core.pagination;

import com.cloud.framework.core.request.Request;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageQueryRequest implements Pagination, Request {

    private Integer pageNo;
    private Integer pageSize;
}
