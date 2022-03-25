package com.mosect.parser4java.java;

import com.mosect.parser4java.core.common.CommonToken;

/**
 * 字符Token
 */
public class CharToken extends CommonToken {

    private final char value;

    protected CharToken(String type, String text, char value) {
        super(type, text);
        this.value = value;
    }

    public char getValue() {
        return value;
    }
}
