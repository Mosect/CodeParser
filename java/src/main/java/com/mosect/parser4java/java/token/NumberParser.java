package com.mosect.parser4java.java.token;

import com.mosect.parser4java.core.Token;
import com.mosect.parser4java.java.Constants;

public class NumberParser extends com.mosect.parser4java.javalike.NumberParser {

    @Override
    public String getName() {
        return Constants.PARSER_NUMBER;
    }

    @Override
    public Token makeToken() {
        String text = getParseText().toString();
        return new NumberToken(
                Constants.TOKEN_NUMBER,
                text,
                getValue(),
                getRadix(),
                isInteger()
        );
    }
}
