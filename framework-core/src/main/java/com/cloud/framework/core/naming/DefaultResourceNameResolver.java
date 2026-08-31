package com.cloud.framework.core.naming;

public class DefaultResourceNameResolver implements ResourceNameResolver {

    @Override
    public String resolve(String... name) {
        return String.join(delimiter(), name);
    }
}
