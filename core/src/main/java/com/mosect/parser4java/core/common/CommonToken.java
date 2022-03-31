package com.mosect.parser4java.core.common;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.Token;

/**
 * 通用token
 */
public class CommonToken implements Token {

    private final String type;
    private final String text;
    private Node parent;

    public CommonToken(String type, String text) {
        this.type = type;
        this.text = text;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Node getParent() {
        return parent;
    }

    @Override
    public void setParent(Node parent) {
        this.parent = parent;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "CommonToken{" +
                "type='" + type + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
