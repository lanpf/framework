package com.cloud.framework.core.error;

import lombok.Getter;

@Getter
public enum FrameworkError implements BaseError {
    // ===== 兜底 =====
    FRAMEWORK_ERROR(0, "框架异常"),

    // ===== 序列化/反序列化 =====
    SERIALIZATION_FAILED(1, "序列化或反序列化失败"),

    // ===== 资源与容量 =====
    RESOURCE_EXHAUSTED(10, "资源耗尽"),
    THREAD_POOL_REJECTED(11, "线程池拒绝执行"),
    CONNECTION_POOL_EXHAUSTED(12, "连接池耗尽"),

    // ===== 流量防护 / 服务保护 =====
    RATE_LIMIT_EXCEEDED(100, "请求限流"),
    CONCURRENCY_LIMIT_EXCEEDED(101, "并发数超限"),
    REQUEST_TIMEOUT(102, "请求超时"),
    CIRCUIT_BREAKER_OPEN(110, "服务熔断"),
    SERVICE_DEGRADED(111, "服务降级"),

    // ===== 远程调用 / 网络通信 =====
    REMOTE_CALL_FAILED(200, "远程调用失败"),
    CONNECT_TIMEOUT(201, "连接建立超时"),
    READ_TIMEOUT(202, "远程读取超时"),
    REMOTE_SERVICE_UNAVAILABLE(203, "远程服务不可用"),
    REMOTE_RESPONSE_ERROR(204, "远程响应异常"),

    // ===== 分布式锁 =====
    LOCK_ACQUIRE_FAILED(300, "加锁失败"),
    LOCK_WAIT_TIMEOUT(301, "等待锁超时"),
    LOCK_RENEWAL_FAILED(302, "锁续期失败"),
    LOCK_RELEASE_FAILED(303, "锁释放失败"),

    // ===== 消息队列 / 异步处理 =====
    MESSAGE_SEND_FAILED(400, "消息发送失败"),
    MESSAGE_CONSUME_FAILED(401, "消息消费失败"),
    MESSAGE_RETRY_EXHAUSTED(402, "消息重试耗尽"),
    MESSAGE_ACK_FAILED(403, "消息确认失败"),
    MESSAGE_PARTITION_KEY_MISSING(404, "分区键缺失");


    private final int localCode;
    private final String message;

    FrameworkError(int localCode, String message) {
        ErrorCodeRange.assertLocalCode(localCode);
        this.localCode = localCode;
        this.message = message;
    }

    @Override
    public ErrorCodePrefix getPrefix() {
        return new ErrorCodePrefix(ErrorCodeNamespace.FRAMEWORK, 0);
    }
}
