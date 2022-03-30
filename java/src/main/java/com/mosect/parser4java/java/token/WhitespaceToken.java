package com.mosect.parser4java.java.token;

import com.mosect.parser4java.core.common.CommonToken;

public class WhitespaceToken extends CommonToken {

    private final String charName;

    protected WhitespaceToken(String type, String text, String charName) {
        super(type, text);
        this.charName = charName;
    }

    public String getCharName() {
        return charName;
    }
}
