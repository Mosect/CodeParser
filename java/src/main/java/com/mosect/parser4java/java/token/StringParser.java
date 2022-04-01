package com.mosect.parser4java.java.token;

import com.mosect.parser4java.core.Token;
import com.mosect.parser4java.java.NameConstants;

public class StringParser extends com.mosect.parser4java.javalike.StringParser {

    @Override
    public String getName() {
        return NameConstants.PARSER_STRING;
    }

    @Override
    public Token makeToken() {
        String text = getParseText().toString();
        return new StringToken(NameConstants.TOKEN_STRING, text, getString());
    }
}
