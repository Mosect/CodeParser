package com.mosect.parser4java.java.node;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.node.NodeParser;
import com.mosect.parser4java.java.Constants;

/**
 * 节点解析器
 */
public class JavaNodeParser extends NodeParser {

    @Override
    protected Node createRootNode() {
        return new DocumentNode(Constants.NODE_DOCUMENT);
    }

    @Override
    protected boolean isIgnoreNode(Node node) {
        if (node.isToken()) {
            switch (node.getType()) {
                case Constants.TOKEN_COMMENT:
                case Constants.TOKEN_WHITESPACE:
                    return true;
            }
        }
        return super.isIgnoreNode(node);
    }
}
