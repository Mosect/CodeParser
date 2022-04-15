package com.mosect.parser4java.java.node;

import com.mosect.parser4java.core.common.CommonNode;

/**
 * 括号节点
 */
public class BracketNode extends CommonNode {

    private final String bracketType;

    public BracketNode(String type, String bracketType) {
        super(type);
        this.bracketType = bracketType;
    }

    public String getBracketType() {
        return bracketType;
    }
}
