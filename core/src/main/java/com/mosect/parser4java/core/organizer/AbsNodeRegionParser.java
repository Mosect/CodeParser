package com.mosect.parser4java.core.organizer;

public abstract class AbsNodeRegionParser implements NodeRegionParser {

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public NodeRegion matchRegion(NodeContext context, NodeRegion parent, int nodeStart, int nodeIndex) {
        return null;
    }

    @Override
    public boolean processRegion(NodeContext context, NodeRegion region, int nodeIndex, int nodeEnd) {
        // 获取父节点区域解析器优先级
        int pp = region.getParent().getParser().getPriority();
        if (getPriority() < pp) {
            // 本解析器优先级小于父解析器，先处理父解析器
            boolean ok = onProcessParentRegion(context, region, nodeIndex, nodeEnd);
            if (!ok) {
                // 父解析器无法进行有效解析，解析当前区域
                ok = onProcessRegion(context, region, nodeIndex, nodeEnd);
                if (ok) {
                    // 处理成功
                    if (region.isHeadOk() && region.isTailOk()) {
                        // 区域已关闭
                        onRegionClosed(context, region);
                    }
                }
            }
            return ok;
        } else {
            // 本解析器优先级高，先处理本解析器
            boolean ok = onProcessRegion(context, region, nodeIndex, nodeEnd);
            if (ok) {
                // 处理成功
                if (region.isHeadOk() && region.isTailOk()) {
                    // 区域已关闭
                    onRegionClosed(context, region);
                }
            } else {
                // 处理父解析器
                ok = onProcessParentRegion(context, region, nodeIndex, nodeEnd);
            }
            return ok;
        }
    }

    /**
     * 处理父节点区域
     *
     * @param context   节点上下文
     * @param region    节点区域：子区域
     * @param nodeIndex 需要处理的节点下标
     * @param nodeEnd   节点结束位置
     * @return true，表示进行了有效处理；false，无法处理
     */
    protected boolean onProcessParentRegion(NodeContext context, NodeRegion region, int nodeIndex, int nodeEnd) {
        NodeRegion parent = region.getParent();
        NodeRegionParser parser = parent.getParser();
        boolean ok = parser.processRegion(context, parent, nodeIndex, nodeEnd);
        if (ok) {
            if (parent.isHeadOk() && parent.isTailOk()) {
                // 父节点已关闭，强制关闭当前节点
                onForceCloseRegion(context, region, parent.getTailStart());
            }
        }
        return ok;
    }

    /**
     * 处理节点区域
     *
     * @param context   节点上下文
     * @param region    节点区域
     * @param nodeIndex 需要处理的节点下标
     * @param nodeEnd   节点结束位置
     * @return true，表示进行了有效处理；false，无法处理
     */
    protected boolean onProcessRegion(NodeContext context, NodeRegion region, int nodeIndex, int nodeEnd) {
        return false;
    }

    /**
     * 强制关闭节点区域
     *
     * @param context 节点上下文
     * @param region  节点区域
     * @param nodeEnd 节点结束位置
     */
    protected void onForceCloseRegion(NodeContext context, NodeRegion region, int nodeEnd) {
        if (!region.isHeadOk()) {
            region.setHeadEnd(nodeEnd);
            region.setHeadOk(true);
        }
        if (region.getTailStart() < region.getHeadEnd()) {
            region.setTailStart(region.getHeadEnd());
        }
        region.setTailEnd(nodeEnd);
        region.setTailOk(true);
        onRegionClosed(context, region);
    }

    /**
     * 节点被关闭时触发
     *
     * @param context 节点上下文
     * @param region  节点区域
     */
    protected void onRegionClosed(NodeContext context, NodeRegion region) {
        context.removeUnclosedRegion(region.getUnclosedIndex());
    }
}
