package com.mosect.parser4java.core.util;

/**
 * 字符工具
 */
public final class CharUtils {

    private CharUtils() {
    }

    public static boolean isDecChar(char ch) {
        return ch >= '0' && ch <= '9';
    }

    public static boolean isHexChar(char ch) {
        return (ch >= 'a' && ch <= 'f') || (ch >= 'A' && ch <= 'F') || isDecChar(ch);
    }

    public static boolean isOctChar(char ch) {
        return ch >= '0' && ch <= '7';
    }

    public static boolean isBinChar(char ch) {
        return ch == '0' || ch == '1';
    }
}
