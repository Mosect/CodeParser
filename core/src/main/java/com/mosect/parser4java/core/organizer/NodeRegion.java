package com.mosect.parser4java.core.organizer;

public class NodeRegion {

    private final NodeRegion parent; // 父区域
    private final NodeRegionParser parser; // 解析器
    private int headStart = -1; // 头部开始位置
    private int headEnd = -1; // 头部结束位置
    private boolean headOk = false; // 头部是否已解析完成
    private int tailStart = -1; // 尾部开始位置
    private int tailEnd = -1; // 尾部结束位置
    private boolean tailOk = false; // 尾部是否已解析完成
    private int index = -1;
    private int unclosedIndex = -1;

    public NodeRegion(NodeRegion parent, NodeRegionParser parser) {
        this.parent = parent;
        this.parser = parser;
    }

    public NodeRegion getParent() {
        return parent;
    }

    public NodeRegionParser getParser() {
        return parser;
    }

    public int getHeadStart() {
        return headStart;
    }

    public void setHeadStart(int headStart) {
        this.headStart = headStart;
    }

    public int getHeadEnd() {
        return headEnd;
    }

    public void setHeadEnd(int headEnd) {
        this.headEnd = headEnd;
    }

    public boolean isHeadOk() {
        return headOk;
    }

    public void setHeadOk(boolean headOk) {
        this.headOk = headOk;
    }

    public int getTailStart() {
        return tailStart;
    }

    public void setTailStart(int tailStart) {
        this.tailStart = tailStart;
    }

    public int getTailEnd() {
        return tailEnd;
    }

    public void setTailEnd(int tailEnd) {
        this.tailEnd = tailEnd;
    }

    public boolean isTailOk() {
        return tailOk;
    }

    public void setTailOk(boolean tailOk) {
        this.tailOk = tailOk;
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
