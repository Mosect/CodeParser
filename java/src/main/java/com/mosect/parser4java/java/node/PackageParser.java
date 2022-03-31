package com.mosect.parser4java.java.node;

import com.mosect.parser4java.core.Token;
import com.mosect.parser4java.java.token.KeywordToken;

/**
 * 包节点解析器
 */
public class PackageParser extends BaseParser {

    protected ClassNameParser classNameParser = new ClassNameParser();

    @Override
    public String getName() {
        return "java.package";
    }

    @Override
    protected void onClear() {
        super.onClear();
    }

    @Override
    protected boolean isNodeStart(int offset, Token token) {
        return token instanceof KeywordToken && "package".equals(token.getText());
    }

    @Override
    protected boolean isNodeEnd(int offset, Token token) {
        return "java.symbol".equals(token.getType()) && ";".equals(token.getText());
    }

    @Override
    protected int onProcessNodeValidToken(int offset, Token token) {
        return 0;
    }
}
