package com.mosect.parser4java.java.organizer;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.Token;
import com.mosect.parser4java.core.organizer.NodeContext;
import com.mosect.parser4java.core.organizer.NodeRegion;
import com.mosect.parser4java.java.NameConstants;
import com.mosect.parser4java.java.node.BraceNode;

/**
 * 花括号区域
 */
public class BraceNodeRegionHandler extends BaseNodeRegionHandler {

    @Override
    public String getName() {
        return NameConstants.REGION_HANDLER_BRACE;
    }

    @Override
    public Node createNode(NodeContext context, NodeRegion parent, int start, int offset) {
        return new BraceNode(NameConstants.NODE_BRACE);
    }

    @Override
    protected boolean isRegionStartSymbol(Token token) {
        return "{".equals(token.getText());
    }

    @Override
    protected boolean isRegionEndSymbol(Token token) {
        return "}".equals(token.getText());
    }

    @Override
    public boolean isMustEndWithNode() {
        return true;
    }

    @Override
    public void onRegionClosed(NodeContext context, NodeRegion region) {
        super.onRegionClosed(context, region);
        NodeRegion parent = region.getParent();
        if (null != parent && parent.getUnclosedIndex() >= 0) {
            if (NameConstants.REGION_HANDLER_TYPE.equals(parent.getHandler().getName())) {
                // 类 父节点，关闭父节点
                context.getOrganizer().closeRegion(context, parent.getUnclosedIndex(), region.getEnd());
            }
        }
    }
}
