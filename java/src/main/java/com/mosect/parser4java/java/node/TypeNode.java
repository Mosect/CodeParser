package com.mosect.parser4java.java.node;

import com.mosect.parser4java.core.common.CommonNode;

/**
 * 类型节点
 */
public class TypeNode extends CommonNode {

    /**
     * 类
     */
    public final static String CLASS_TYPE_CLASS = "class";
    /**
     * 接口
     */
    public final static String CLASS_TYPE_INTERFACE = "interface";
    /**
     * 注解
     */
    public final static String CLASS_TYPE_ANNOTATION = "annotation";
    /**
     * 枚举
     */
    public final static String CLASS_TYPE_ENUM = "enum";

    private final String classType;

    public TypeNode(String type, String classType) {
        super(type);
        this.classType = classType;
    }

    public String getClassType() {
        return classType;
    }
}
