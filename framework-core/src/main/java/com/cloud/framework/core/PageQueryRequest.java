package com.cloud.framework.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 仅包含分页参数的通用请求实现。
 *
 * <p>需要同时携带客户端、渠道或其他业务条件的请求不应继承此类型。</p>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageQueryRequest implements Pagination, Request {

    private Integer pageNo;
    private Integer pageSize;
}
