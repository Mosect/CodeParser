package com.mosect.lib.codeparser.java;

import com.mosect.lib.codeparser.Token;

public class CharToken extends Token {

    private final char value;

    public CharToken(String type, String text, char value) {
        super(type, text);
        this.value = value;
    }

    public char getValue() {
        return value;
    }

    @Override
    public CharToken copy() {
        CharToken token = new CharToken(getType(), getText(), getValue());
        token.setError(getError());
        return token;
    }
}
