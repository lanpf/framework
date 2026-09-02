package com.cloud.framework.core.naming;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * 资源名解析契约：把稳定的业务键段解析为最终资源名。
 *
 * <p>分隔符默认为 {@link #DELIMITER}；命名空间相关的注入与组合由具体实现承担。</p>
 */
@FunctionalInterface
public interface ResourceNameResolver {

    String DELIMITER = ":";

    String resolve(String... name);

    default String delimiter() {
        return DELIMITER;
    }

    default String[] normalize(String... names) {
        return Arrays.stream(names)
                .filter(StringUtils::isNotBlank)
                .toArray(String[]::new);
    }
}
