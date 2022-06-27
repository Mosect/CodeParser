package com.mosect.lib.codeparser.java;

import com.mosect.lib.codeparser.AbsNodeMatcher;
import com.mosect.lib.codeparser.Node;

public abstract class JavaNodeMatcher extends AbsNodeMatcher {

    @Override
    protected boolean isCodeNode(Node node) {
        return !node.isToken() || (!"comment".equals(node.getType()) && !"whitespace".equals(node.getType()));
    }
}
