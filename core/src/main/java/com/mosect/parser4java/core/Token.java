package com.mosect.parser4java.core;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public interface Token extends Node {

    @Override
    default int getChildCount() {
        return 0;
    }

    @Override
    default Node getChild(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    default boolean addChild(Node child) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void addChild(int index, Node child) {
        throw new UnsupportedOperationException();
    }

    @Override
    default boolean removeChild(Node child) {
        throw new UnsupportedOperationException();
    }

    @Override
    default Node removeChild(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    default Node setChild(int index, Node child) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void clearAllChild() {
        throw new UnsupportedOperationException();
    }

    @Override
    default boolean addChildren(Collection<Node> children) {
        throw new UnsupportedOperationException();
    }

    @Override
    default boolean isToken() {
        return true;
    }

    @Override
    default int getCharCount() {
        return getText().length();
    }

    @Override
    default void append(Appendable out) throws IOException {
        out.append(getText());
    }

    @Override
    default boolean check(List<ParseError> outErrors) {
        return true;
    }

    @Override
    default Iterator<Node> iterator() {
        return new Iterator<Node>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Node next() {
                return null;
            }
        };
    }

    /**
     * 获取token文本
     *
     * @return token文本
     */
    String getText();
}
