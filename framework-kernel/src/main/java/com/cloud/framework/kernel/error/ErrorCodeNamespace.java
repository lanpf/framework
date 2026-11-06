package com.cloud.framework.kernel.error;

/**
 * 按错误契约的归属划分六位错误码的前三位。接入层返回框架或业务服务的错误时保留原码。
 */
public enum ErrorCodeNamespace implements ErrorCodeRange {
    /** 前缀 100–199，用于框架定义的通用错误和能力错误。 */
    FRAMEWORK(100, 199),
    /** 前缀 200–299，用于网关及业务服务的入站适配器自己定义的接入错误。 */
    EDGE(200, 299),
    /** 前缀 300–899，用于业务服务定义的领域、应用和基础设施错误。 */
    BIZ(300, 899);

    private final int minPrefix;
    private final int maxPrefix;

    ErrorCodeNamespace(int minPrefix, int maxPrefix) {
        this.minPrefix = minPrefix;
        this.maxPrefix = maxPrefix;
    }

    public int getMinPrefix() {
        return minPrefix;
    }

    public int getMaxPrefix() {
        return maxPrefix;
    }

    @Override
    public int getMin() {
        return minPrefix * LOCAL_CODE_BASE;
    }

    @Override
    public int getMax() {
        return (maxPrefix + 1) * LOCAL_CODE_BASE - 1;
    }

    public void assertPrefix(int value) {
        if (value < minPrefix || value > maxPrefix) {
            throw FrameworkException.illegalArgument(
                    String.format("%d out of prefix range [%d, %d]", value, minPrefix, maxPrefix)
            );
        }
    }
}
