package com.mosect.parser4java.core.common;

import com.mosect.parser4java.core.Node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 通用节点
 */
public class CommonNode implements Node {

    private final String type;
    private List<Node> children;

    public CommonNode(String type) {
        this.type = type;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public boolean isToken() {
        return false;
    }

    @Override
    public int getChildCount() {
        if (null != children) return children.size();
        return 0;
    }

    @Override
    public List<Node> getChildren() {
        if (null == children) children = createChildren();
        return children;
    }

    @Override
    public void append(Appendable out) throws IOException {
        if (getChildCount() > 0) {
            for (Node child : getChildren()) {
                child.append(out);
            }
        }
    }

    @Override
    public Iterator<Node> iterator() {
        return getChildren().iterator();
    }

    protected List<Node> createChildren() {
        return new ArrayList<>();
    }
}
