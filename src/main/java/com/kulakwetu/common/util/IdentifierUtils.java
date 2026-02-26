package com.kulakwetu.common.util;

import java.util.regex.Pattern;

public final class IdentifierUtils {
    private static final Pattern PHONE = Pattern.compile("^\\+[1-9][0-9]{6,14}$");

    private IdentifierUtils() {}

    public static boolean isPhone(String value) {
        return PHONE.matcher(value).matches();
    }

    public static boolean isEmail(String value) {
        return value.contains("@");
    }
}
