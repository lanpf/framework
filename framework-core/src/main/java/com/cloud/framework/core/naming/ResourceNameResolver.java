package com.cloud.framework.core.naming;

import com.cloud.framework.kernel.validation.Require;

import java.util.Arrays;
import java.util.Objects;

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
        return Arrays.stream(Require.notNull(names, "Resource name segments must not be null"))
                .filter(Objects::nonNull)
                .filter(name -> !name.isBlank())
                .toArray(String[]::new);
    }
}
