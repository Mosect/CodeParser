package com.mosect.parser4java.java.token;

import com.mosect.parser4java.core.Token;
import com.mosect.parser4java.java.NameConstants;

public class NumberParser extends com.mosect.parser4java.javalike.NumberParser {

    @Override
    public String getName() {
        return NameConstants.PARSER_NUMBER;
    }

    @Override
    public Token makeToken() {
        String text = getParseText().toString();
        return new NumberToken(
                NameConstants.TOKEN_NUMBER,
                text,
                getValue(),
                getRadix(),
                isInteger()
        );
    }
}
