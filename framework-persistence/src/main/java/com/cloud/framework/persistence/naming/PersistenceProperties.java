package com.cloud.framework.persistence.naming;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersistenceProperties {
    private String database = "mysql";

    private final Naming naming = new Naming();

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Naming {
        private String tablePrefix;
        private String tableSuffix = "_d_o";
    }
}
