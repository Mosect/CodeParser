package com.mosect.lib.codeparser.gradle;

import com.mosect.lib.codeparser.Node;
import com.mosect.lib.codeparser.java.JavaNodeMatcher;
import com.mosect.lib.codeparser.util.NodeUtils;

public abstract class GradleNodeMatcher extends JavaNodeMatcher {

    @Override
    protected boolean isCodeNode(Node node) {
        if (NodeUtils.matchLine(node)) {
            return true;
        }
        return super.isCodeNode(node);
    }
}
