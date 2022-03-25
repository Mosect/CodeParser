package com.mosect.parser4java.java;

import com.mosect.parser4java.core.common.CommonToken;

/**
 * 字符串token
 */
public class StringToken extends CommonToken {

    private final String string;

    protected StringToken(String type, String text, String string) {
        super(type, text);
        this.string = string;
    }

    public String getString() {
        return string;
    }
}
