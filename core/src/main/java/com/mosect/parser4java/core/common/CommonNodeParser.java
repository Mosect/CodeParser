package com.mosect.parser4java.core.common;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.NodeParser;
import com.mosect.parser4java.core.NodeSource;
import com.mosect.parser4java.core.ParseError;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用节点解析器
 */
public abstract class CommonNodeParser implements NodeParser {

    private NodeParser parent;
    private NodeSource source;
    private int nodeStart;
    private Node node;
    private final List<ParseError> errors = new ArrayList<>();

    @Override
    public boolean parse(NodeParser parent, NodeSource source) {
        this.parent = parent;
        boolean newStart = false;
        if (this.source != source || nodeStart != source.getStart()) {
            this.source = source;
            this.nodeStart = source.getStart();
            onClear();
            newStart = true;
        }
        if (source.hasMore()) {
            return onParse(source, newStart);
        }
        return false;
    }

    @Override
    public ParseError getError(int index) {
        return errors.get(index);
    }

    @Override
    public int getErrorCount() {
        return errors.size();
    }

    @Override
    public Node getNode() {
        return node;
    }

    @Override
    public NodeSource getSource() {
        return source;
    }

    protected NodeParser getParent() {
        return parent;
    }

    protected void onClear() {
        errors.clear();
        node = null;
    }

    protected void setNode(Node node) {
        this.node = node;
    }

    /**
     * 添加错误
     *
     * @param errorId       错误id
     * @param errorMsg      错误信息
     * @param errorPosition 错误位置
     */
    protected void putError(String errorId, String errorMsg, int errorPosition) {
        errors.add(new ParseError(errorId, errorMsg, errorPosition));
    }

    /**
     * 添加错误
     *
     * @param error 错误对象
     */
    protected void putError(ParseError error) {
        errors.add(error);
    }

    protected abstract boolean onParse(NodeSource source, boolean newStart);
}
