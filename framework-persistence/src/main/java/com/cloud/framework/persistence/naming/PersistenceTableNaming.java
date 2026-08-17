package com.cloud.framework.persistence.naming;

public final class PersistenceTableNaming {
    private PersistenceTableNaming() {
    }

    public static String apply(String name, String prefix, String suffix) {
        return addPrefix(removeSuffix(name, suffix), prefix);
    }

    private static String addPrefix(String name, String prefix) {
        if (name != null && prefix != null && !name.startsWith(prefix)) {
            return prefix + name;
        }
        return name;
    }

    private static String removeSuffix(String name, String suffix) {
        if (name == null || suffix == null || suffix.isBlank()) {
            return name;
        }
        if (name.endsWith(suffix)) {
            return name.substring(0, name.length() - suffix.length());
        }
        return name;
    }
}
