package com.mosect.parser4java.core.organizer;

import com.mosect.parser4java.core.Node;

import java.util.ArrayList;
import java.util.List;

public class NodeOrganizer {

    private final List<NodeRegionHandler> handlers = new ArrayList<>();

    /**
     * 组织节点
     *
     * @param source 源
     * @param offset 偏移位置
     * @return 节点列表
     */
    public List<Node> organize(List<? extends Node> source, int offset) {
        NodeContext context = new NodeContext(source);
        return organize(null, context, offset, offset);
    }

    /**
     * 组织节点
     *
     * @param parent  父区域
     * @param context 上下文
     * @param start   开始位置
     * @param offset  偏移位置，即正式处理的位置
     * @return 节点列表
     */
    public List<Node> organize(NodeRegion parent, NodeContext context, int start, int offset) {
        int curOffset = offset;
        int curStart = start;
        List<? extends Node> source = context.getSource();
        while (curOffset < source.size()) {
            // 查找开始的节点区域
            int regionStart = -1;
            NodeRegionHandler regionHandler = null;
            for (NodeRegionHandler handler : getHandlers()) {
                regionStart = handler.getRegionStart(this, context, parent, curStart, curOffset);
                if (regionStart >= 0) {
                    regionHandler = handler;
                    break;
                }
            }

            int checkUnclosedRegionCount = context.getUnclosedRegionCount();
            if (regionStart >= 0) {
                // 存在开始的节点区域，创建响应区域

                // 获取安全的父区域
                NodeRegion safeParent = parent;
                if (context.getUnclosedRegionCount() > 0) {
                    // 从未关闭区域中选择父区域
                    safeParent = context.getUnclosedRegion(context.getUnclosedRegionCount() - 1);
                }

                Node node = regionHandler.createNode(this, context, safeParent, curStart, curOffset);
                NodeRegion nodeRegion = new NodeRegion(node, regionHandler, parent);
                nodeRegion.setStart(curStart);
                nodeRegion.setEnd(curOffset + 1);
                context.addRegion(nodeRegion);
                onRegionAdded(context, nodeRegion);
                // 检查此区域是否完成
                if (!regionHandler.isRegionEnd(this, context, nodeRegion, curOffset)) {
                    // 区域未结束
                    context.addUnclosedRegion(nodeRegion);
                }

                // 需要更改开始位置
                curStart = curOffset + 1;
            }

            // 检查未关闭的节点区域
            int closedIndex = -1; // 已关闭的区域下标
            for (int i = checkUnclosedRegionCount - 1; i >= 0; i--) {
                NodeRegion region = context.getUnclosedRegion(i);
                NodeRegionHandler handler = region.getHandler();
                if (handler.isRegionEnd(this, context, region, curOffset)) {
                    // 节点结束
                    closedIndex = i;
                }
            }
            if (closedIndex >= 0) {
                // 存在已关闭的区域下标
                int count = context.getUnclosedRegionCount();
                // 关闭之后的所有区域
                for (int i = closedIndex; i < count; i++) {
                    NodeRegion region = context.getUnclosedRegion(i);
                    region.setEnd(curOffset + 1);
                }
                // 移除已关闭的区域
                context.removeLastUncloseRegions(count - closedIndex);
            }

            ++curOffset;
        }

        int regionIndex = 0;

    }

    /**
     * 当有新的节点区域被添加时触发
     *
     * @param context 节点上下文
     * @param region  节点区域
     */
    protected void onRegionAdded(NodeContext context, NodeRegion region) {
    }

    public List<NodeRegionHandler> getHandlers() {
        return handlers;
    }

    public void addHandler(NodeRegionHandler handler) {
        handlers.add(handler);
    }
}
