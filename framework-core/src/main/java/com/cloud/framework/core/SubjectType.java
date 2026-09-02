package com.cloud.framework.core;

/**
 * 受入口层写入的认证主体类型。
 *
 * <p>作为 {@link RequestHeader#SUBJECT_TYPE} 的取值契约，区分宿主登录会话与受限浏览器会话；
 * 下游不得把缺失或未知类型默认解释为宿主会话。</p>
 */
public enum SubjectType {
    HOST_SESSION,
    BROWSER_SESSION
}
