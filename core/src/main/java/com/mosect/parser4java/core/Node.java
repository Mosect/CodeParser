package com.mosect.parser4java.core;

import java.util.List;

/**
 * 节点
 */
public interface Node extends Iterable<Node> {

    String getId();

    String getName();

    String getType();

    boolean isToken();

    int getChildCount();

    List<Node> getChildren();
}
