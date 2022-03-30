package com.mosect.parser4java.java.token;

import com.mosect.parser4java.core.Token;

public class WhitespaceParser extends com.mosect.parser4java.javalike.WhitespaceParser {

    private WhitespaceTokenFactory whitespaceTokenFactory = new WhitespaceTokenFactory();

    @Override
    public String getName() {
        return "java.whitespace";
    }

    @Override
    public Token makeToken() {
        String text = getParseText().toString();
        return getWhitespaceTokenFactory().findTokenByText(text);
    }

    public WhitespaceTokenFactory getWhitespaceTokenFactory() {
        return whitespaceTokenFactory;
    }

    protected void setWhitespaceTokenFactory(WhitespaceTokenFactory whitespaceTokenFactory) {
        this.whitespaceTokenFactory = whitespaceTokenFactory;
    }
}
