package com.cloud.framework.core.error;

import lombok.Getter;

@Getter
public enum FrameworkError implements BaseError {
    ILLEGAL_ARGUMENT(1, "Illegal argument"),
    ILLEGAL_STATE(2, "Illegal state"),

    // ===== 序列化/反序列化 =====
    SERIALIZATION_FAILED(10, "Serialization or deserialization failed"),

    // ===== 资源与容量 =====
    RESOURCE_EXHAUSTED(90, "Resource exhausted"),
    THREAD_POOL_REJECTED(91, "Thread pool rejected the task"),
    CONNECTION_POOL_EXHAUSTED(92, "Connection pool exhausted"),

    // ===== 流量防护 / 服务保护 =====
    RATE_LIMIT_EXCEEDED(100, "Rate limit exceeded"),
    CONCURRENCY_LIMIT_EXCEEDED(101, "Concurrency limit exceeded"),
    REQUEST_TIMEOUT(102, "Request timed out"),
    CIRCUIT_BREAKER_OPEN(110, "Circuit breaker is open"),
    SERVICE_DEGRADED(111, "Service is degraded"),

    // ===== 远程调用 / 网络通信 =====
    REMOTE_CALL_FAILED(200, "Remote call failed"),
    CONNECT_TIMEOUT(201, "Connection timed out"),
    READ_TIMEOUT(202, "Read timed out"),
    REMOTE_SERVICE_UNAVAILABLE(203, "Remote service unavailable"),
    REMOTE_RESPONSE_ERROR(204, "Remote response error"),

    // ===== 分布式锁 =====
    LOCK_ACQUIRE_FAILED(300, "Failed to acquire lock"),
    LOCK_WAIT_TIMEOUT(301, "Lock wait timed out"),
    LOCK_RENEWAL_FAILED(302, "Failed to renew lock"),
    LOCK_RELEASE_FAILED(303, "Failed to release lock"),

    // ===== 消息队列 / 异步处理 =====
    MESSAGE_SEND_FAILED(400, "Failed to send message"),
    MESSAGE_CONSUME_FAILED(401, "Message consumption failed"),
    MESSAGE_RETRY_EXHAUSTED(402, "Message retries exhausted"),
    MESSAGE_ACK_FAILED(403, "Message acknowledgment failed"),
    MESSAGE_PARTITION_KEY_MISSING(404, "Message partition key is missing");


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
