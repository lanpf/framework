package com.cloud.framework.core.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCodeNamespace implements ErrorCodeRange {
    FRAMEWORK(100, 100),
    CORE(101, 199),
    BIZ(200, 799),
    EDGE(800, 899),
    RESERVED(900, 999);

    private final int minPrefix;
    private final int maxPrefix;

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
            throw new IllegalArgumentException(
                    String.format("%d out of prefix range [%d, %d]", value, minPrefix, maxPrefix)
            );
        }
    }
}
