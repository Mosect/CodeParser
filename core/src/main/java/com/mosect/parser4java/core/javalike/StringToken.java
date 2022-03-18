package com.mosect.parser4java.core.javalike;

import com.mosect.parser4java.core.common.CommonToken;

/**
 * 字符串token
 */
public class StringToken extends CommonToken {

    private final String string;

    protected StringToken(String name, String text, String string) {
        super("", name, "string", text);
        this.string = string;
    }

    /**
     * 获取字符串
     *
     * @return 字符串
     */
    public String getString() {
        return string;
    }
}
