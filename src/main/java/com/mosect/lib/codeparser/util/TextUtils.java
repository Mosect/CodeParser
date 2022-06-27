package com.mosect.lib.codeparser.util;

public final class TextUtils {

    private TextUtils() {
    }

    public static boolean empty(CharSequence cs) {
        return null == cs || cs.length() <= 0;
    }

    public static boolean notEmpty(CharSequence cs) {
        return null != cs && cs.length() > 0;
    }

    public static String showText(String text) {
        if (notEmpty(text)) {
            StringBuilder builder = new StringBuilder(text.length());
            for (int i = 0; i < text.length(); i++) {
                char ch = text.charAt(i);
                switch (ch) {
                    case '\r':
                        builder.append("\\r");
                        break;
                    case '\n':
                        builder.append("\\n");
                        break;
                    case '\t':
                        builder.append("\\t");
                        break;
                    case '\b':
                        builder.append("\\b");
                        break;
                    case '\f':
                        builder.append("\\f");
                        break;
                    case '\\':
                        builder.append("\\\\");
                        break;
                    case '\0':
                        builder.append("\\0");
                        break;
                    default:
                        builder.append(ch);
                        break;
                }
            }
            return builder.toString();
        }
        return "";
    }

    public static boolean match(CharSequence text, int offset, int end, String str) {
        if (str.length() + offset <= end && end <= text.length()) {
            for (int i = 0; i < str.length(); i++) {
                if (text.charAt(offset + i) != str.charAt(i)) return false;
            }
            return true;
        }
        return false;
    }

    public static boolean isDecChar(char value) {
        return value >= '0' && value <= '9';
    }

    public static boolean isHexChar(char value) {
        return (value >= 'a' && value <= 'f') || (value >= 'A' && value <= 'F') || isDecChar(value);
    }

    public static boolean isOctChar(char value) {
        return value >= '0' && value <= '7';
    }
}
