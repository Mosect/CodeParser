package com.mosect.parser4java.java.node;

import com.mosect.parser4java.java.Constants;

/**
 * 圆括号节点解析器
 */
public class CurvesBracketNodeParser extends BracketNodeParser {

    @Override
    protected String getBracketType() {
        return Constants.BRACKET_CURVES;
    }

    @Override
    protected String getNodeStartSymbol() {
        return "(";
    }

    @Override
    protected String getNodeEndSymbol() {
        return ")";
    }
}
