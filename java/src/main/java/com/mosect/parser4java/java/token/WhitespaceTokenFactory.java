package com.mosect.parser4java.java.token;

import com.mosect.parser4java.java.Constants;

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
        register(new WhitespaceToken(Constants.TOKEN_WHITESPACE, text, charName));
    }

    protected void unregister(String text) {
        WhitespaceToken token = tokenMap.remove(text);
        if (null != token) {
            tokenNameMap.remove(token.getCharName());
        }
    }

    public WhitespaceToken createTokenByText(String text) {
        WhitespaceToken token = tokenMap.get(text);
        if (null != token) {
            return new WhitespaceToken(token);
        }
        return null;
    }

    public WhitespaceToken createTokenByName(String charName) {
        WhitespaceToken token = tokenNameMap.get(charName);
        if (null != token) {
            return new WhitespaceToken(token);
        }
        return null;
    }
}
