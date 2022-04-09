package com.mosect.parser4java.core.organizer;

import com.mosect.parser4java.core.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * 节点组织器
 */
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
        // 处理源节点
        onHandleContextSource(context, parent, start, offset);
        // 处理未关闭的区域
        int unclosedCount = context.getUnclosedRegionCount();
        if (unclosedCount > 0) {
            for (int i = unclosedCount - 1; i >= 0; i--) {
                NodeRegion region = context.getUnclosedRegion(i);
                int end = region.getHandler().getForceEndIndex(this, context, region);
                region.setEnd(end);
                onRegionClosed(context, region);
            }
            context.removeLastUncloseRegions(unclosedCount);
        }

        // 组合节点
        return makeNodeList(context);
    }

    protected void onHandleContextSource(NodeContext context, NodeRegion parent, int start, int offset) {
        int curOffset = offset;
        List<? extends Node> source = context.getSource();
        while (curOffset < source.size()) {
            int curStart = getCurrentStart(context, start);
            boolean consumed = onHandleRegionStart(context, parent, curStart, curOffset);
            if (!consumed) {
                onHandleRegionEnd(context, curOffset);
            }

            ++curOffset;
        }
    }

    protected int getCurrentStart(NodeContext context, int start) {
        int curStart;
        if (context.getRegionCount() > 0) {
            NodeRegion lastRegion = context.getRegion(context.getRegionCount() - 1);
            if (lastRegion.getUnclosedIndex() < 0) {
                // 已闭合
                curStart = lastRegion.getEnd();
            } else {
                // 未闭合
                curStart = lastRegion.getStart();
            }
        } else {
            curStart = start;
        }
        return curStart;
    }

    /**
     * 处理节点区域开始位置
     *
     * @param context 节点上下文
     * @param parent  父节点区域
     * @param start   源节点开始偏移量
     * @param offset  需要处理的源节点偏移量
     * @return 是否消耗偏移位置的源节点
     */
    protected boolean onHandleRegionStart(NodeContext context, NodeRegion parent, int start, int offset) {
        int regionStart = -1;
        NodeRegionHandler regionHandler = null;
        for (NodeRegionHandler handler : getHandlers()) {
            regionStart = handler.getRegionStart(this, context, parent, start, offset);
            if (regionStart >= 0) {
                regionHandler = handler;
                break;
            }
        }

        NodeRegionHandler.EndState endState = null;
        if (regionStart >= 0) {
            // 存在开始的节点区域，创建相应区域

            // 获取安全的父区域
            NodeRegion safeParent = parent;
            if (context.getUnclosedRegionCount() > 0) {
                // 从未关闭区域中选择父区域
                safeParent = context.getUnclosedRegion(context.getUnclosedRegionCount() - 1);
            }

            Node node = regionHandler.createNode(this, context, safeParent, start, offset);
            NodeRegion nodeRegion = new NodeRegion(node, regionHandler, parent);
            nodeRegion.setStart(regionStart);
            nodeRegion.setEnd(offset + 1);
            context.addRegion(nodeRegion);
            onRegionAdded(context, nodeRegion);
            // 检查此区域是否完成
            endState = regionHandler.getRegionEnd(this, context, nodeRegion, offset);
            if (endState == NodeRegionHandler.EndState.NONE) {
                // 区域未结束
                context.addUnclosedRegion(nodeRegion);
            }
            return true;
        }

        return false;
    }

    /**
     * 处理区域结束位置
     *
     * @param context 节点上下文
     * @param offset  源节点偏移量
     * @return 是否消耗偏移位置的源节点
     */
    protected boolean onHandleRegionEnd(NodeContext context, int offset) {
        int checkUnclosedRegionCount = context.getUnclosedRegionCount();
        int closedIndex = -1; // 已关闭的区域下标
        boolean consumed = false;
        for (int i = checkUnclosedRegionCount - 1; i >= 0; i--) {
            NodeRegion region = context.getUnclosedRegion(i);
            NodeRegionHandler handler = region.getHandler();
            NodeRegionHandler.EndState regionEndState = handler.getRegionEnd(this, context, region, offset);
            if (regionEndState != NodeRegionHandler.EndState.NONE) {
                // 节点结束
                closedIndex = i;
                if (regionEndState == NodeRegionHandler.EndState.CLOSED_AND_CONSUMED) {
                    // 当前位置已消耗
                    consumed = true;
                    break;
                }
            }
        }
        if (closedIndex >= 0) {
            // 存在已关闭的区域下标
            int count = context.getUnclosedRegionCount();
            // 关闭之后的所有区域
            for (int i = count - 1; i >= closedIndex; i--) {
                NodeRegion region = context.getUnclosedRegion(i);
                region.setEnd(offset + 1);
                onRegionClosed(context, region);
            }
            // 移除已关闭的区域
            context.removeLastUncloseRegions(count - closedIndex);
        }
        return consumed;
    }

    /**
     * 当有新的节点区域被添加时触发
     *
     * @param context 节点上下文
     * @param region  节点区域
     */
    protected void onRegionAdded(NodeContext context, NodeRegion region) {
    }

    /**
     * 节点被关闭时触发
     *
     * @param context 节点上下文
     * @param region  节点区域
     */
    protected void onRegionClosed(NodeContext context, NodeRegion region) {
    }

    /**
     * 关闭区域
     *
     * @param context 节点上下文
     * @param region  节点区域
     * @param end     节点结束位置
     */
    public void closeRegion(NodeContext context, NodeRegion region, int end) {
        if (region.getUnclosedIndex() >= 0) {
            for (int i = context.getUnclosedRegionCount() - 1; i >= region.getUnclosedIndex(); i--) {
                NodeRegion unclosed = context.getUnclosedRegion(i);
                unclosed.setEnd(end);
                onRegionClosed(context, unclosed);
            }
            context.removeLastUncloseRegions(context.getUnclosedRegionCount() - region.getUnclosedIndex());
        } else {
            region.setEnd(end);
            onRegionClosed(context, region);
        }
    }

    /**
     * 设置节点结束位置
     *
     * @param region 节点区域
     * @param end    结束位置
     */
    public void setRegionEnd(NodeRegion region, int end) {
        region.setEnd(end);
    }

    /**
     * 构建节点列表
     *
     * @param context 上下文
     * @return 节点列表
     */
    protected List<Node> makeNodeList(NodeContext context) {
        List<? extends Node> source = context.getSource();
        int count = context.getRegionCount();
        if (count > 0) {
            // 存在节点区域，进行节点整合
            List<Node> result = new ArrayList<>(Math.max(12, count / 2));
            int regionIndex = 0; // 区域下标
            int addedRegionIndex = -1;
            for (int i = 0; i < source.size(); i++) {
                Node srcNode = source.get(i);
                // 寻找符合的区域
                NodeRegion region = null; // 当前节点区域
                for (int ri = regionIndex; ri < count; ri++) {
                    NodeRegion cr = context.getRegion(ri);
                    if (i >= cr.getStart()) {
                        regionIndex = ri;
                        region = cr;
                    } else {
                        break;
                    }
                }

                if (null == region) {
                    // 无区域匹配
                    result.add(srcNode);
                } else {
                    // 存在匹配区域
                    if (null == region.getParent() && addedRegionIndex != regionIndex) {
                        result.add(region.getNode());
                        addedRegionIndex = regionIndex;
                    }
                    if (i < region.getEnd()) {
                        // 符合区域
                        region.getNode().addChild(srcNode);
                    } else {
                        // 不符合区域
                        addToParent(result, region, srcNode, i);
                    }
                }
            }
            return result;
        } else {
            // 不存在节点区域，返回原本节点列表
            return new ArrayList<>(source);
        }
    }

    private void addToParent(List<Node> root, NodeRegion region, Node srcNode, int index) {
        NodeRegion parent = region.getParent();
        if (null != parent) {
            if (index >= parent.getStart() && index < parent.getEnd()) {
                parent.getNode().addChild(srcNode);
            } else {
                addToParent(root, parent, srcNode, index);
            }
        } else {
            root.add(srcNode);
        }
    }

    public List<NodeRegionHandler> getHandlers() {
        return handlers;
    }

    public void addHandler(NodeRegionHandler handler) {
        handlers.add(handler);
    }
}
