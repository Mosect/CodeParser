package com.mosect.parser4java.core.util;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.NodeList;

import java.util.ArrayList;
import java.util.List;

public class ArrayNodeList implements NodeList {

    private final List<Node> list;

    public ArrayNodeList(List<? extends Node> list) {
        this.list = (List<Node>) list;
    }

    public ArrayNodeList(int initialCapacity) {
        list = new ArrayList<>(initialCapacity);
    }

    @Override
    public int getNodeCount() {
        return list.size();
    }

    @Override
    public Node getNode(int index) {
        return list.get(index);
    }

    public List<Node> getList() {
        return list;
    }
}
