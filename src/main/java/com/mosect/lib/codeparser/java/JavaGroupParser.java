package com.mosect.lib.codeparser.java;

import com.mosect.lib.codeparser.Group;
import com.mosect.lib.codeparser.Node;
import com.mosect.lib.codeparser.Token;
import com.mosect.lib.codeparser.util.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class JavaGroupParser {

    public void adjust(Node node) {
        if (!node.isToken()) {
            List<Node> nodes = node.getChildren();
            List<Node> list = parse(nodes, 0, nodes.size());
            nodes.clear();
            nodes.addAll(list);
        }
    }

    public List<Node> parse(List<Node> nodes, int start, int end) {
        List<Group> groups = new ArrayList<>();
        List<Node> result = new ArrayList<>(8);
        for (int i = start; i < end; i++) {
            Node node = nodes.get(i);
            if (groups.size() > 0) {
                Group group = groups.get(groups.size() - 1);
                if (isGroupEnd(group, node)) {
                    group.getChildren().add(node);
                    groups.remove(groups.size() - 1);
                    continue;
                }
            }

            String groupType = getGroupTypeByBeginNode(node);
            if (TextUtils.notEmpty(groupType)) {
                Group parent = groups.size() > 0 ? groups.get(groups.size() - 1) : null;
                Group group = new Group();
                group.setType(groupType);
                group.getChildren().add(node);
                groups.add(group);
                if (null == parent) {
                    result.add(group);
                } else {
                    parent.getChildren().add(group);
                }
            } else {
                Group parent = groups.size() > 0 ? groups.get(groups.size() - 1) : null;
                if (null == parent) {
                    result.add(node);
                } else {
                    parent.getChildren().add(node);
                }
            }
        }
        return result;
    }

    protected String getGroupTypeByBeginNode(Node node) {
        if (node.isToken() && "symbol".equals(node.getType())) {
            String text = ((Token) node).getText();
            switch (text) {
                case "{":
                    return "{}";
                case "(":
                    return "()";
                default:
                    return null;
            }
        }
        return null;
    }

    protected boolean isGroupEnd(Group group, Node node) {
        if (node.isToken() && "symbol".equals(node.getType())) {
            String text = ((Token) node).getText();
            if ("()".equals(group.getType()) && ")".equals(text)) {
                return true;
            }
            if ("{}".equals(group.getType()) && "}".equals(text)) {
                return true;
            }
        }
        return false;
    }
}
