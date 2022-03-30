package com.mosect.parser4java.java.token;

import com.mosect.parser4java.core.Token;
import com.mosect.parser4java.core.common.CommonToken;

public class SymbolParser extends com.mosect.parser4java.javalike.SymbolParser {

    @Override
    public String getName() {
        return "java.symbol";
    }

    @Override
    public Token makeToken() {
        String text = getParseText().toString();
        return new CommonToken(getName(), text);
    }
}
