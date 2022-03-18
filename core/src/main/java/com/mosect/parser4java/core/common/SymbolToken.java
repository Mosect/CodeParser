package com.mosect.parser4java.core.common;

public class SymbolToken extends CommonToken {

    public SymbolToken(String id, String name, String text) {
        super(id, name, "symbol", text);
    }

    public SymbolToken(String text) {
        this(text, "symbol:" + text, text);
    }
}
