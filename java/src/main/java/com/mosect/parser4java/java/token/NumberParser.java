package com.mosect.parser4java.java.token;

import com.mosect.parser4java.core.Token;

public class NumberParser extends com.mosect.parser4java.javalike.NumberParser {

    @Override
    public String getName() {
        return "java.number";
    }

    @Override
    public Token makeToken() {
        String text = getParseText().toString();
        return new NumberToken(
                getName(),
                text,
                getValue(),
                getRadix(),
                isInteger()
        );
    }
}
