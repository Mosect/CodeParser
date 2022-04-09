package com.mosect.parser4java.core.organizer;

import com.mosect.parser4java.core.Node;

/**
 * 节点区域
 */
public class NodeRegion {

    private final Node node;
    private final NodeRegionHandler handler;
    private final NodeRegion parent;
    private int start;
    private int end;
    private int index = -1;
    private int unclosedIndex = -1;

    NodeRegion(Node node, NodeRegionHandler handler, NodeRegion parent) {
        this.node = node;
        this.handler = handler;
        this.parent = parent;
    }

    public Node getNode() {
        return node;
    }

    public NodeRegionHandler getHandler() {
        return handler;
    }

    public NodeRegion getParent() {
        return parent;
    }

    public int getStart() {
        return start;
    }

    void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    void setEnd(int end) {
        this.end = end;
    }

    public int getIndex() {
        return index;
    }

    void setIndex(int index) {
        this.index = index;
    }

    public int getUnclosedIndex() {
        return unclosedIndex;
    }

    void setUnclosedIndex(int unclosedIndex) {
        this.unclosedIndex = unclosedIndex;
    }
}
