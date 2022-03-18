package com.mosect.parser4java.core.javalike;

import com.mosect.parser4java.core.common.CommonToken;

public class CharToken extends CommonToken {

    private final CharType charType;
    private final char value;

    protected CharToken(String name, String text, CharType charType, char value) {
        super("", name, "char", text);
        this.charType = charType;
        this.value = value;
    }

    public char getValue() {
        return value;
    }

    public CharType getCharType() {
        return charType;
    }
}
