package com.mosect.parser4java.java.token;

import com.mosect.parser4java.core.Token;
import com.mosect.parser4java.java.Constants;

public class StringParser extends com.mosect.parser4java.javalike.StringParser {

    @Override
    public String getName() {
        return Constants.PARSER_STRING;
    }

    @Override
    public Token makeToken() {
        String text = getParseText().toString();
        return new StringToken(Constants.TOKEN_STRING, text, getString());
    }
}
