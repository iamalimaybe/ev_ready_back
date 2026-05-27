package com.ev.ready.common.util;

public final class StringUtil {

    private StringUtil() {
    }

    public static String trim(String value) {
        return value == null ? null : value.trim();
    }

    public static String trimToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
