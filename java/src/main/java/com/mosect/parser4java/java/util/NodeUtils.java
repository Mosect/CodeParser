package com.mosect.parser4java.java.util;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.Token;
import com.mosect.parser4java.core.organizer.NodeContext;
import com.mosect.parser4java.java.NameConstants;
import com.mosect.parser4java.java.token.KeywordToken;

import java.util.List;
import java.util.Objects;

public final class NodeUtils {

    private NodeUtils() {
    }

    public static boolean isKeywordNode(Node node, String keyword) {
        if (node.isToken() && NameConstants.TOKEN_KEYWORD.equals(node.getType())) {
            return Objects.equals(((KeywordToken) node).getText(), keyword);
        }
        return false;
    }

    public static boolean isSymbolNode(Node node, String symbol) {
        if (node.isToken() && NameConstants.TOKEN_SYMBOL.equals(node.getType())) {
            return Objects.equals(((Token) node).getText(), symbol);
        }
        return false;
    }

    public static int trimStart(NodeContext context, int start, int end) {
        List<? extends Node> source = context.getSource();
        int trimStart = start;
        for (int i = start; i < end; i++) {
            Node srcNode = source.get(i);
            if (srcNode.isToken()) {
                if (NameConstants.TOKEN_COMMENT.equals(srcNode.getType()) ||
                        NameConstants.TOKEN_WHITESPACE.equals(srcNode.getType())) {
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

    public static int trimEnd(NodeContext context, int start, int end) {
        List<? extends Node> source = context.getSource();
        int trimEnd = end;
        for (int i = end - 1; i >= start; i--) {
            Node srcNode = source.get(i);
            if (srcNode.isToken()) {
                if (NameConstants.TOKEN_COMMENT.equals(srcNode.getType()) ||
                        NameConstants.TOKEN_WHITESPACE.equals(srcNode.getType())) {
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
