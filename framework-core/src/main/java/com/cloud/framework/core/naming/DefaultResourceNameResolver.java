package com.cloud.framework.core.naming;

/** 默认实现：跳过空白键段后按分隔符连接。 */
public class DefaultResourceNameResolver implements ResourceNameResolver {

    @Override
    public String resolve(String... names) {
        return String.join(delimiter(), normalize(names));
    }
}
