package com.mosect.parser4java.java.util;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.Token;
import com.mosect.parser4java.java.NameConstants;
import com.mosect.parser4java.java.token.KeywordToken;

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
}
