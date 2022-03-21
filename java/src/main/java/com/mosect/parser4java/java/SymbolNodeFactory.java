package com.mosect.parser4java.java;

public class SymbolNodeFactory extends com.mosect.parser4java.core.javalike.SymbolNodeFactory {

    public SymbolNodeFactory() {
        // 以下符号放在SymbolNodeFactory2中
        unregisterSymbol(".");
        unregisterSymbol("-");
        unregisterSymbol("+");
    }
}
