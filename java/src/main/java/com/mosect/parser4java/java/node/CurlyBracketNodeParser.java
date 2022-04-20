package com.mosect.parser4java.java.node;

import com.mosect.parser4java.java.Constants;

/**
 * 花括号节点解析器
 */
public class CurlyBracketNodeParser extends BracketNodeParser {

    @Override
    protected String getBracketType() {
        return Constants.BRACKET_CURLY;
    }

    @Override
    protected String getNodeStartSymbol() {
        return "{";
    }

    @Override
    protected String getNodeEndSymbol() {
        return "}";
    }
}
