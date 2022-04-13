package com.mosect.parser4java.java.node;

import com.mosect.parser4java.core.common.CommonNode;

/**
 * 类型节点
 */
public class TypeNode extends CommonNode {

    private final String classType;

    public TypeNode(String type, String classType) {
        super(type);
        this.classType = classType;
    }

    public String getClassType() {
        return classType;
    }
}
