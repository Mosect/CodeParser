package com.mosect.parser4java.java.token;

import com.mosect.parser4java.java.NameConstants;

import java.util.HashMap;
import java.util.Map;

public class WhitespaceTokenFactory {

    private final Map<String, WhitespaceToken> tokenMap = new HashMap<>();
    private final Map<String, WhitespaceToken> tokenNameMap = new HashMap<>();

    public WhitespaceTokenFactory() {
        register("\r\n", "crlf");
        register("\r", "cr");
        register("\n", "lf");
        register("\t", "tab");
        register(" ", "space");
    }

    protected void register(WhitespaceToken token) {
        tokenMap.put(token.getText(), token);
        tokenNameMap.put(token.getCharName(), token);
    }

    protected void register(String text, String charName) {
        register(new WhitespaceToken(NameConstants.TOKEN_WHITESPACE, text, charName));
    }

    protected void unregister(String text) {
        WhitespaceToken token = tokenMap.remove(text);
        if (null != token) {
            tokenNameMap.remove(token.getCharName());
        }
    }

    public WhitespaceToken findTokenByText(String text) {
        return tokenMap.get(text);
    }

    public WhitespaceToken findTokenByName(String charName) {
        return tokenNameMap.get(charName);
    }
}
