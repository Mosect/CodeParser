package com.mosect.parser4java.java.node;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.NodeList;
import com.mosect.parser4java.core.Token;
import com.mosect.parser4java.core.node.NodeInfo;
import com.mosect.parser4java.core.node.UnclosedNodes;
import com.mosect.parser4java.java.Constants;
import com.mosect.parser4java.java.util.NodeUtils;

import java.util.Objects;

public class BlockNodeParser extends JavaNodeParser {

    @Override
    protected void createNode(NodeList src, int start, int end, NodeInfo out) {
        int index = end - 1;
        Node node = src.getNode(index);
        if (node.isToken()) {
            Token token = (Token) node;
            switch (token.getType()) {
                case Constants.TOKEN_KEYWORD:
                    switch (token.getText()) {
                        case "class": // 类
                            out.set(start, new TypeNode(Constants.NODE_TYPE, Constants.CLASS_TYPE_CLASS), false);
                            break;
                        case "interface": // 接口
                            int symbolIndex = NodeUtils.beforeToken(Constants.TOKEN_SYMBOL, "@", src, start, index);
                            if (symbolIndex >= 0) {
                                // 注解类
                                out.set(start, new TypeNode(Constants.NODE_TYPE, Constants.CLASS_TYPE_ANNOTATION), false);
                            } else {
                                // 接口类
                                out.set(start, new TypeNode(Constants.NODE_TYPE, Constants.CLASS_TYPE_INTERFACE), false);
                            }
                            break;
                        case "enum": // 枚举
                            out.set(start, new TypeNode(Constants.NODE_TYPE, Constants.CLASS_TYPE_ENUM), false);
                            break;
                    }
                    break;
                case Constants.TOKEN_SYMBOL:
                    break;
            }
        } else {
        }
    }

    @Override
    protected boolean isBrotherWithUnclosedNode(Node unclosedNode, Node node) {
        if (Objects.equals(unclosedNode.getType(), node.getType())) {
            if (Constants.NODE_TYPE.equals(node.getType())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected int getNodeEnd(UnclosedNodes unclosedNodes, NodeList src, int start, int end) {
        Node node = src.getNode(end - 1);
        String curType = unclosedNodes.current().getType();
        switch (curType) {
            case Constants.NODE_TYPE: // 类节点
                if (Constants.NODE_BRACKET.equals(node.getType())) {
                    BracketNode bracketNode = (BracketNode) node;
                    if (Constants.BRACKET_CURLY.equals(bracketNode.getBracketType())) {
                        // 花括号
                        return end;
                    }
                }
                break;
        }
        return -1;
    }
}
