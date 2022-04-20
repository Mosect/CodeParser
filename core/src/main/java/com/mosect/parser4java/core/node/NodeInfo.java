package com.mosect.parser4java.core.node;

import com.mosect.parser4java.core.Node;

public class NodeInfo {

    private boolean exists;
    private int start;
    private Node node;
    private boolean closed;

    public void clear() {
        exists = false;
    }

    public void set(int start, Node node, boolean closed) {
        exists = true;
        this.start = start;
        this.node = node;
        this.closed = closed;
    }

    public boolean isExists() {
        return exists;
    }

    public int getStart() {
        return start;
    }

    public Node getNode() {
        return node;
    }

    public boolean isClosed() {
        return closed;
    }
}
