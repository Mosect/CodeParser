package com.mosect.lib.codeparser.java;

import com.mosect.lib.codeparser.Token;

public class StringToken extends Token {

    private final String value;

    public StringToken(String type, String text, String value) {
        super(type, text);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public StringToken copy() {
        StringToken token = new StringToken(getType(), getText(), getValue());
        token.setError(getError());
        return token;
    }
}
