package com.mosect.parser4java.java.token;

import com.mosect.parser4java.core.Token;

public class CharParser extends com.mosect.parser4java.javalike.CharParser {

    @Override
    public String getName() {
        return "java.char";
    }

    @Override
    public Token makeToken() {
        String text = getParseText().toString();
        return new CharToken(getName(), text, getValue());
    }
}
