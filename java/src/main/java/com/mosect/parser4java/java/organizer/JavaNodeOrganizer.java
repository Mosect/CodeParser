package com.mosect.parser4java.java.organizer;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.organizer.NodeContext;
import com.mosect.parser4java.core.organizer.NodeOrganizer;
import com.mosect.parser4java.core.organizer.NodeRegion;
import com.mosect.parser4java.core.organizer.NodeRegionHandler;
import com.mosect.parser4java.java.util.NodeUtils;

import java.util.List;

public class JavaNodeOrganizer extends NodeOrganizer {

    public JavaNodeOrganizer() {
        addHandler(new PackageNodeRegionHandler());
        addHandler(new ImportNodeRegionHandler());
    }

    @Override
    protected void onHandleContextSource(NodeContext context, NodeRegion parent, int start, int offset) {
        int curOffset = offset;
        List<? extends Node> source = context.getSource();
        while (curOffset < source.size()) {
            int curStart = getCurrentStart(context, start);
            int oldRegionCount = context.getRegionCount();
            boolean consumed = onHandleRegionEnd(context, curOffset);
            if (!consumed) {
                onHandleRegionStart(context, parent, curStart, curOffset);
                if (context.getRegionCount() > oldRegionCount) {
                    // 添加了节点区域
                    curStart = curOffset + 1;
                }
            }

            ++curOffset;
        }
    }

    @Override
    protected void onRegionAdded(NodeContext context, NodeRegion region) {
        super.onRegionAdded(context, region);
        if (context.getUnclosedRegionCount() > 0) {
            NodeRegion lastUnclosed = context.getUnclosedRegion(context.getUnclosedRegionCount() - 1);
            NodeRegionHandler unclosedHandler = lastUnclosed.getHandler();
            if (unclosedHandler instanceof BaseNodeRegionHandler) {
                if (!((BaseNodeRegionHandler) unclosedHandler).isMustEndWithNode()) {
                    if (((BaseNodeRegionHandler) unclosedHandler).isBrother(region.getHandler())) {
                        // 兄弟关系，介绍未关闭的区域
                        int end = NodeUtils.trimEnd(context, lastUnclosed.getStart(), region.getStart());
                        closeRegion(context, lastUnclosed, end);
                    }
                }
            }
        }
    }
}
