package com.quyen.hust.util;

import org.springframework.util.StringUtils;

public class StringUtil {
    public static String escapeWildCardCharacter(String str) {
        if (!StringUtils.hasText(str)) {
            return null;
        }
        return str.trim()
                .replace("%", "\\%")
                .replace("[", "\\[")
                .replace("]", "\\]")
                .replace("{", "\\{")
                .replace("}", "\\}")
                .replace("-", "\\-")
                .replace("^", "\\^")
                .replace("_", "\\_");
    }
}
