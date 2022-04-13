package com.mosect.parser4java.java.util;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.NodeList;
import com.mosect.parser4java.core.Token;
import com.mosect.parser4java.java.Constants;
import com.mosect.parser4java.java.token.KeywordToken;

import java.util.Objects;

public final class NodeUtils {

    private NodeUtils() {
    }

    public static boolean isKeywordNode(Node node, String keyword) {
        if (node.isToken() && Constants.TOKEN_KEYWORD.equals(node.getType())) {
            return Objects.equals(((KeywordToken) node).getText(), keyword);
        }
        return false;
    }

    public static boolean isSymbolNode(Node node, String symbol) {
        if (node.isToken() && Constants.TOKEN_SYMBOL.equals(node.getType())) {
            return Objects.equals(((Token) node).getText(), symbol);
        }
        return false;
    }

    public static int trimStart(NodeList src, int start, int end) {
        int trimStart = start;
        for (int i = start; i < end; i++) {
            Node srcNode = src.getNode(i);
            if (srcNode.isToken()) {
                if (Constants.TOKEN_COMMENT.equals(srcNode.getType()) ||
                        Constants.TOKEN_WHITESPACE.equals(srcNode.getType())) {
                    trimStart = i + 1;
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        return trimStart;
    }

    public static int trimEnd(NodeList src, int start, int end) {
        int trimEnd = end;
        for (int i = end - 1; i >= start; i--) {
            Node srcNode = src.getNode(i);
            if (srcNode.isToken()) {
                if (Constants.TOKEN_COMMENT.equals(srcNode.getType()) ||
                        Constants.TOKEN_WHITESPACE.equals(srcNode.getType())) {
                    trimEnd = i + 1;
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        return trimEnd;
    }
}
