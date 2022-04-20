package com.mosect.parser4java.core.common;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.NodeList;
import com.mosect.parser4java.core.ParseError;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * 通用节点
 */
public class CommonNode implements Node {

    private Node parent;
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

    private List<Node> getChildren() {
        if (null == children) children = new ArrayList<>();
        return children;
    }

    @Override
    public int getChildCount() {
        if (null != children) return children.size();
        return 0;
    }

    @Override
    public Node getChild(int index) {
        if (null != children) {
            return children.get(index);
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public void addChild(int index, Node child) {
        checkParentExist(child);
        getChildren().add(index, child);
        child.setParent(this);
    }

    @Override
    public boolean addChild(Node child) {
        checkParentExist(child);
        boolean ok = getChildren().add(child);
        if (ok) {
            child.setParent(this);
        }
        return ok;
    }

    @Override
    public boolean removeChild(Node child) {
        if (null != children) {
            boolean ok = children.remove(child);
            if (ok) {
                child.setParent(null);
            }
            return ok;
        }
        return false;
    }

    @Override
    public Node removeChild(int index) {
        if (null != children) {
            Node old = children.remove(index);
            if (null != old) {
                old.setParent(null);
            }
            return old;
        }
        return null;
    }

    @Override
    public Node setChild(int index, Node child) {
        checkParentExist(child);
        child.setParent(this);
        return getChildren().set(index, child);
    }

    @Override
    public void clearAllChild() {
        if (null != children) {
            for (Node child : children) {
                child.setParent(null);
            }
            children.clear();
        }
    }

    @Override
    public boolean addChildren(Collection<Node> children) {
        for (Node child : children) {
            checkParentExist(child);
        }
        boolean ok = getChildren().addAll(children);
        if (ok) {
            for (Node child : children) {
                child.setParent(this);
            }
        }
        return ok;
    }

    @Override
    public void addChildren(NodeList children) {
        for (int i = 0; i < children.getNodeCount(); i++) {
            Node node = children.getNode(i);
            addChild(node);
        }
    }

    @Override
    public Node getParent() {
        return parent;
    }

    @Override
    public void setParent(Node parent) {
        this.parent = parent;
    }

    @Override
    public int getCharCount() {
        int len = 0;
        for (Node child : getChildren()) {
            len += child.getCharCount();
        }
        return len;
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
    public boolean check(List<ParseError> outErrors) {
        return true;
    }

    @Override
    public Iterator<Node> iterator() {
        return getChildren().iterator();
    }

    protected List<Node> createChildren() {
        return new ArrayList<>();
    }

    private void checkParentExist(Node child) {
        if (null != child.getParent()) {
            throw new IllegalStateException("Child exist parent");
        }
    }
}
