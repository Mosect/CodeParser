package com.mosect.parser4java.core.common;

import com.mosect.parser4java.core.Node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 通用节点
 */
public class CommonNode implements Node {

    private String id;
    private String name;
    private String type;
    private List<Node> children;

    public CommonNode(String id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public CommonNode() {
    }

    @Override
    public String getId() {
        return id;
    }

    protected void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    @Override
    public String getType() {
        return type;
    }

    protected void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean isToken() {
        return false;
    }

    @Override
    public int getChildCount() {
        return null == children ? 0 : children.size();
    }

    @Override
    public List<Node> getChildren() {
        if (null == children) children = new ArrayList<>();
        return children;
    }

    @Override
    public Iterator<Node> iterator() {
        return children.iterator();
    }
}
