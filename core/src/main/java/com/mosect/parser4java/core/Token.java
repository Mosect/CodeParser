package com.mosect.parser4java.core;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public interface Token extends Node {

    @Override
    default int getChildCount() {
        return 0;
    }

    @Override
    default List<Node> getChildren() {
        return Collections.emptyList();
    }

    @Override
    default boolean isToken() {
        return true;
    }

    @Override
    default void append(Appendable out) throws IOException {
        out.append(getText());
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
