package com.mosect.parser4java.core.javalike;

/**
 * 字符类型
 */
public enum CharType {
    /**
     * 普通字符
     */
    NORMAL,
    /**
     * 转义字符
     */
    ESCAPED,
    /**
     * \0 字符
     */
    ZERO,
    /**
     * u码16进制，\uxxxx格式
     */
    HEX,
    /**
     * u码8进制，\xxx格式
     */
    OCT,
}
