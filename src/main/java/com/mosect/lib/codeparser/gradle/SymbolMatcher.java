package com.mosect.lib.codeparser.gradle;

public class SymbolMatcher extends com.mosect.lib.codeparser.java.SymbolMatcher {

    public SymbolMatcher() {
        register("==~").register("..<");
        sort();
    }
}
