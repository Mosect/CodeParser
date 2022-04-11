package com.mosect.parser4java.java.organizer;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.organizer.NodeContext;
import com.mosect.parser4java.core.organizer.NodeRegion;
import com.mosect.parser4java.java.NameConstants;
import com.mosect.parser4java.java.node.TypeNode;
import com.mosect.parser4java.java.token.KeywordToken;
import com.mosect.parser4java.java.util.NodeUtils;

/**
 * 类型节点
 */
public class TypeNodeRegionHandler extends BaseNodeRegionHandler {

    private String classType;

    @Override
    public String getName() {
        return NameConstants.REGION_HANDLER_TYPE;
    }

    @Override
    public Node createNode(NodeContext context, NodeRegion parent, int start, int offset) {
        return new TypeNode(NameConstants.NODE_TYPE, classType);
    }

    @Override
    protected int getRegionStartWithKeyword(NodeContext context, NodeRegion parent, int start, int offset, KeywordToken token) {
        switch (token.getText()) {
            case "class":
                classType = TypeNode.CLASS_TYPE_CLASS;
                return NodeUtils.trimStart(context, start, offset);
            case "enum":
                classType = TypeNode.CLASS_TYPE_ENUM;
                return NodeUtils.trimStart(context, start, offset);
            case "interface":
                classType = TypeNode.CLASS_TYPE_INTERFACE;
                return NodeUtils.trimStart(context, start, offset);
            case "@interface":
                classType = TypeNode.CLASS_TYPE_ANNOTATION;
                return NodeUtils.trimStart(context, start, offset);
        }
        return -1;
    }
}
