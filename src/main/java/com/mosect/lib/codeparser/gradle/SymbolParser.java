package com.mosect.lib.codeparser.gradle;

public class SymbolParser extends com.mosect.lib.codeparser.java.SymbolParser {

    public SymbolParser() {
        symbolMatcher = new SymbolMatcher();
    }
}
