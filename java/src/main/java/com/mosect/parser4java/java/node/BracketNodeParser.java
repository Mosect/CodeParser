package com.mosect.parser4java.java.node;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.NodeList;
import com.mosect.parser4java.core.node.NodeInfo;
import com.mosect.parser4java.core.node.UnclosedNodes;
import com.mosect.parser4java.java.Constants;
import com.mosect.parser4java.java.util.NodeUtils;

/**
 * 括号解析器
 */
public abstract class BracketNodeParser extends JavaNodeParser {

    @Override
    protected void createNode(NodeList src, int start, int end, NodeInfo out) {
        int index = end - 1;
        Node node = src.getNode(index);
        if (NodeUtils.isSymbolNode(node, getNodeStartSymbol())) {
            out.set(index, new BracketNode(Constants.NODE_BRACKET, getBracketType()), false);
        }
    }

    @Override
    protected int getNodeEnd(UnclosedNodes unclosedNodes, NodeList src, int start, int end) {
        int index = end - 1;
        Node node = src.getNode(index);
        if (NodeUtils.isSymbolNode(node, getNodeEndSymbol())) {
            return end;
        }
        return -1;
    }

    protected abstract String getBracketType();

    protected abstract String getNodeStartSymbol();

    protected abstract String getNodeEndSymbol();
}
