package com.mosect.lib.codeparser.util;

import com.mosect.lib.codeparser.Node;
import com.mosect.lib.codeparser.Token;
import com.mosect.lib.codeparser.java.CharToken;
import com.mosect.lib.codeparser.java.StringToken;

public final class NodeUtils {

    private NodeUtils() {
    }

    public static boolean matchToken(Node node, String type, String text) {
        if (node.isToken()) {
            if (TextUtils.empty(type) || type.equals(node.getType())) {
                Token token = (Token) node;
                return TextUtils.empty(text) || text.equals(token.getText());
            }
        }
        return false;
    }

    public static boolean matchWord(Node node, String text) {
        return matchToken(node, "word", text);
    }

    public static boolean matchSymbol(Node node, String text) {
        return matchToken(node, "symbol", text);
    }

    public static boolean matchWhitespace(Node node) {
        return matchToken(node, "whitespace", null);
    }

    public static boolean matchLine(Node node) {
        if (node.isToken() && "whitespace".equals(node.getType())) {
            Token token = (Token) node;
            switch (token.getText()) {
                case "\r":
                case "\n":
                case "\r\n":
                    return true;
            }
        }
        return false;
    }

    public static boolean matchChar(Node node, Character value) {
        if (node.isToken() && "char".equals(node.getType())) {
            CharToken charToken = (CharToken) node;
            return null == value || charToken.getValue() == value;
        }
        return false;
    }

    public static boolean matchString(Node node, String value) {
        if (node.isToken()) {
            if ("string".equals(node.getType()) || node.getType().startsWith("string.")) {
                StringToken stringToken = (StringToken) node;
                return TextUtils.empty(value) || value.equals(stringToken.getValue());
            }
        }
        return false;
    }

    public static boolean matchStringRegex(Node node, String valueRegex) {
        if (node.isToken()) {
            if ("string".equals(node.getType()) || node.getType().startsWith("string.")) {
                StringToken stringToken = (StringToken) node;
                return TextUtils.empty(valueRegex) || stringToken.getValue().matches(valueRegex);
            }
        }
        return false;
    }
}
