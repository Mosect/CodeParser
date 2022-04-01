package com.mosect.parser4java.core;

import java.util.List;

/**
 * 节点源
 */
public class NodeSource {

    private final List<? extends Node> nodes;
    private int start;
    private int offset;

    public NodeSource(List<? extends Node> nodes, int start) {
        this.nodes = nodes;
        newStart(start);
    }

    public int getStart() {
        return start;
    }

    public int getOffset() {
        return offset;
    }

    public void newStart(int start) {
        this.start = start;
        this.offset = start;
    }

    public Node currentNode() {
        return nodes.get(offset);
    }

    public Node startNode() {
        return nodes.get(start);
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void offset(int value) {
        this.offset += value;
    }

    public void offsetOne() {
        ++this.offset;
    }

    public void resetOffset() {
        offset = start;
    }

    public boolean isValidStart() {
        return start < nodes.size();
    }

    public boolean hasMore() {
        return isValidStart() && offset < nodes.size();
    }

    public Node getNode(int index) {
        return nodes.get(index);
    }

    public void setStartAfterOffset() {
        newStart(offset + 1);
    }

    public int computeStartPosition() {
        return computePosition(start);
    }

    public int computeOffsetPosition() {
        return computePosition(offset);
    }

    public int computePosition(int nodeIndex) {
        int pos = 0;
        for (int i = 0; i < nodeIndex; i++) {
            pos += computeNodeLength(nodes.get(i));
        }
        return pos;
    }

    protected int computeNodeLength(Node node) {
        if (node.isToken()) {
            return ((Token) node).getText().length();
        } else {
            int len = 0;
            for (Node child : node) {
                len += computeNodeLength(child);
            }
            return len;
        }
    }
}
