package com.cloud.framework.core.error;

public record ErrorCodePrefix(ErrorCodeNamespace namespace, int offset) implements ErrorCodeRange {

    public ErrorCodePrefix {
        namespace.assertPrefix(namespace.getMinPrefix() + offset);
    }

    public ErrorCodeNamespace getNamespace() {
        return namespace;
    }

    public int getOffset() {
        return offset;
    }

    @Override
    public int getMin() {
        return namespace.getMin() + offset * LOCAL_CODE_BASE;
    }

    @Override
    public int getMax() {
        return getMin() + LOCAL_CODE_BASE - 1;
    }
}
