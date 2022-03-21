package com.mosect.parser4java.java;

import com.mosect.parser4java.core.common.SymbolNodeFactory;

/**
 * 符号节点解析器，主要处理数值会出现的符号
 */
public class SymbolNodeFactory2 extends SymbolNodeFactory {

    public SymbolNodeFactory2() {
        registerSymbol(".");
        registerSymbol("+");
        registerSymbol("-");
    }
}
