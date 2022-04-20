package com.mosect.parser4java.java.node;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.NodeList;
import com.mosect.parser4java.core.Token;
import com.mosect.parser4java.core.node.NodeInfo;
import com.mosect.parser4java.java.Constants;

/**
 * 花括号节点解析器
 */
public class BraceNodeParser extends JavaNodeParser {

    @Override
    protected void createNode(NodeList src, int start, int end, NodeInfo out) {
        int index = end-1;
        Node node = src.getNode(index);
        if (node.isToken()) {
            Token token = (Token) node;
            if ("{".equals(token.getText())) {
                out.set(index, new BracketNode(Constants.NODE_BRACKET, Constants.BRA));
            }
        }
    }
}
