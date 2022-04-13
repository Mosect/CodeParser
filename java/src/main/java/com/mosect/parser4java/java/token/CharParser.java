package com.mosect.parser4java.java.token;

import com.mosect.parser4java.core.Token;
import com.mosect.parser4java.java.Constants;

public class CharParser extends com.mosect.parser4java.javalike.CharParser {

    @Override
    public String getName() {
        return Constants.PARSER_CHAR;
    }

    @Override
    public Token makeToken() {
        String text = getParseText().toString();
        return new CharToken(Constants.TOKEN_CHAR, text, getValue());
    }
}
