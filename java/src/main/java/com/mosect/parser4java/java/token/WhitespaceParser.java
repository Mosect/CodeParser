package com.mosect.parser4java.java.token;

import com.mosect.parser4java.core.Token;
import com.mosect.parser4java.java.NameConstants;

public class WhitespaceParser extends com.mosect.parser4java.javalike.WhitespaceParser {

    private WhitespaceTokenFactory whitespaceTokenFactory = new WhitespaceTokenFactory();

    @Override
    public String getName() {
        return NameConstants.PARSER_WHITESPACE;
    }

    @Override
    public Token makeToken() {
        String text = getParseText().toString();
        return getWhitespaceTokenFactory().createTokenByText(text);
    }

    public WhitespaceTokenFactory getWhitespaceTokenFactory() {
        return whitespaceTokenFactory;
    }

    protected void setWhitespaceTokenFactory(WhitespaceTokenFactory whitespaceTokenFactory) {
        this.whitespaceTokenFactory = whitespaceTokenFactory;
    }
}
