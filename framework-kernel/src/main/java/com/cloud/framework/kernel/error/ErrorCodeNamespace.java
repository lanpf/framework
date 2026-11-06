package com.cloud.framework.kernel.error;

public enum ErrorCodeNamespace implements ErrorCodeRange {
    FRAMEWORK(100, 100),
    CORE(101, 199),
    BIZ(200, 799),
    EDGE(800, 899);

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
