package com.cloud.framework.core.naming;

public class PassthroughResourceNameResolver implements ResourceNameResolver {

    @Override
    public String resolve(String name) {
        return name;
    }
}
