package com.mosect.parser4java.core.node;

import com.mosect.parser4java.core.Node;

import java.util.LinkedList;

public class UnclosedNodes {

    private final LinkedList<Node> nodes = new LinkedList<>();

    public int getCount() {
        return nodes.size();
    }

    public void add(Node node) {
        nodes.addLast(node);
    }

    public Node current() {
        return nodes.getLast();
    }

    public void close(int index) {
        while (nodes.size() > index) {
            nodes.removeLast();
        }
    }

    public void closeLast() {
        if (nodes.size() > 0) {
            nodes.removeLast();
        }
    }
}
