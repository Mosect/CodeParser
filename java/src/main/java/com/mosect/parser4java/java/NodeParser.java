package com.mosect.parser4java.java;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.NodeList;
import com.mosect.parser4java.java.node.DocumentNode;

/**
 * 节点解析器
 */
public class NodeParser {

    public Node parseRoot(NodeList src, int start, int end) {
        DocumentNode documentNode = new DocumentNode(Constants.NODE_DOCUMENT);
        parse(src, start, end, documentNode);
        return documentNode;
    }

    public void parse(NodeList src, int start, int end, Node out) {
        int contentStart = -1;
        Node container = out;
        for (int i = start; i < end; i++) {
            Node node = src.getNode(i);
            if (contentStart < 0) {
                if (isIgnoreNode(node)) {
                    container.addChild(node);
                    continue;
                }
                contentStart = i;
            }

            
        }
    }

    protected boolean isIgnoreNode(Node node) {
        if (node.isToken()) {
            switch (node.getType()) {
                case Constants.TOKEN_COMMENT:
                case Constants.TOKEN_WHITESPACE:
                    return true;
            }
        }
        return false;
    }
}
