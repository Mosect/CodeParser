package com.mosect.parser4java.java.token;

import com.mosect.parser4java.core.Token;
import com.mosect.parser4java.core.common.CommonToken;
import com.mosect.parser4java.java.Constants;

public class SymbolParser extends com.mosect.parser4java.javalike.SymbolParser {

    @Override
    public String getName() {
        return Constants.PARSER_SYMBOL;
    }

    @Override
    public Token makeToken() {
        String text = getParseText().toString();
        return new CommonToken(Constants.TOKEN_SYMBOL, text);
    }
}
