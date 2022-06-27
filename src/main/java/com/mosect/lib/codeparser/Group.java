package com.mosect.lib.codeparser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Group implements Node {

    private String type;
    private List<Node> children;
    private ErrorInfo error;

    @Override
    public ErrorInfo getError() {
        return error;
    }

    @Override
    public void setError(ErrorInfo error) {
        this.error = error;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public final boolean isToken() {
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
    public String toText() {
        StringBuilder builder = new StringBuilder(64);
        try {
            write(builder);
        } catch (IOException ignored) {
        }
        return builder.toString();
    }

    @Override
    public void write(Appendable appendable) throws IOException {
        if (getChildCount() > 0) {
            for (Node child : getChildren()) {
                child.write(appendable);
            }
        }
    }

    @Override
    public Group copy() {
        Group group = new Group();
        group.setType(getType());
        group.setError(getError());
        if (getChildCount() > 0) {
            for (Node child : getChildren()) {
                group.getChildren().add(child.copy());
            }
        }
        return group;
    }
}
