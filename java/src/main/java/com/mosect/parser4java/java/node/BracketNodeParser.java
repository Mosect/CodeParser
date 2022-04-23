package com.mosect.parser4java.java.node;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.NodeList;
import com.mosect.parser4java.core.Token;
import com.mosect.parser4java.core.node.NodeInfo;
import com.mosect.parser4java.core.node.UnclosedNodes;
import com.mosect.parser4java.java.Constants;
import com.mosect.parser4java.java.util.NodeUtils;

/**
 * 括号解析器
 */
public class BracketNodeParser extends JavaNodeParser {

    @Override
    protected void createNode(NodeList src, int start, int end, NodeInfo out) {
        int index = end - 1;
        Node node = src.getNode(index);
        if (node.isToken()) {
            Token token = (Token) node;
            if (Constants.TOKEN_SYMBOL.equals(token.getType())) {
                String bracketType = getBracketType(token);
                if (null != bracketType) {
                    out.set(index, new BracketNode(Constants.NODE_BRACKET, bracketType), false);
                }
            }
        }
    }

    @Override
    protected int getNodeEnd(UnclosedNodes unclosedNodes, NodeList src, int start, int end) {
        String endSymbol = null;
        if (Constants.NODE_BRACKET.equals(unclosedNodes.current().getType())) {
            BracketNode bracketNode = (BracketNode) unclosedNodes.current();
            switch (bracketNode.getBracketType()) {
                case Constants.BRACKET_SQUARE:
                    endSymbol = "]";
                    break;
                case Constants.BRACKET_CURLY:
                    endSymbol = "}";
                    break;
                case Constants.BRACKET_CURVES:
                    endSymbol = ")";
                    break;
            }
        }
        if (null != endSymbol) {
            int index = end - 1;
            Node node = src.getNode(index);
            if (NodeUtils.isSymbolNode(node, endSymbol)) {
                return end;
            }
        }
        return -1;
    }

    protected String getBracketType(Token symbol) {
        switch (symbol.getText()) {
            case "(":
                return Constants.BRACKET_CURVES;
            case "[":
                return Constants.BRACKET_SQUARE;
            case "{":
                return Constants.BRACKET_CURLY;
            default:
                return null;
        }
    }
}
