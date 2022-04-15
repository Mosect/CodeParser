package com.mosect.parser4java.java.organizer;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.NodeList;
import com.mosect.parser4java.core.organizer.NodeRegion;

public class JavaNodeRegion implements NodeRegion {

    private JavaNodeRegion parent;
    private Node node;
    private final NodeList src;
    private int start;
    private int end;
    private boolean closed;
    private String state; // 状态
    private int resetOffset = -1; // 重置当前位置
    private int resetStart = -1; // 重置开始点

    public JavaNodeRegion(Node node, NodeList src, int start, int end) {
        this.node = node;
        this.src = src;
        this.start = start;
        this.end = end;
    }

    public JavaNodeRegion(String state, NodeList src, int start, int end) {
        this.state = state;
        this.src = src;
        this.start = start;
        this.end = end;
    }

    public void setParent(JavaNodeRegion parent) {
        this.parent = parent;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    @Override
    public JavaNodeRegion getParent() {
        return parent;
    }

    @Override
    public Node getNode() {
        return node;
    }

    public NodeList getSrc() {
        return src;
    }

    public void setStart(int start) {
        this.start = start;
    }

    @Override
    public int getStart() {
        return start;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    @Override
    public int getEnd() {
        return end;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setResetOffset(int resetOffset) {
        this.resetOffset = resetOffset;
    }

    public int getResetOffset() {
        return resetOffset;
    }

    public void setResetStart(int resetStart) {
        this.resetStart = resetStart;
    }

    public int getResetStart() {
        return resetStart;
    }
}
