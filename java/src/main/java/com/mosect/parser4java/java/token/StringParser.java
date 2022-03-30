package com.mosect.parser4java.java.token;

import com.mosect.parser4java.core.Token;

public class StringParser extends com.mosect.parser4java.javalike.StringParser {

    @Override
    public String getName() {
        return "java.string";
    }

    @Override
    public Token makeToken() {
        String text = getParseText().toString();
        return new StringToken(getName(), text, getString());
    }
}
