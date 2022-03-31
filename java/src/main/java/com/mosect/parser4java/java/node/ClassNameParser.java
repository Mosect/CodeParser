package com.mosect.parser4java.java.node;

import com.mosect.parser4java.core.Token;

/**
 * 类名解析器
 */
public class ClassNameParser extends BaseParser {
    @Override
    public String getName() {
        return "java.className";
    }

    @Override
    protected int onProcessNodeValidToken(int offset, Token token) {
        return 0;
    }
}
