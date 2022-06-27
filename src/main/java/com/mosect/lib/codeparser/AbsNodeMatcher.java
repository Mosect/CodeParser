package com.mosect.lib.codeparser;

import java.util.List;

public abstract class AbsNodeMatcher implements NodeMatcher {

    @Override
    public boolean matchNodes(Node node, int start, int end) {
        if (node.getChildCount() > 0) {
            return matchNodes(node.getChildren(), start, end);
        }
        return false;
    }

    @Override
    public boolean matchNodes(List<Node> nodes, int start, int end) {
        onReset();
        for (int i = start; i < end; i++) {
            Node child = nodes.get(i);
            if (isCodeNode(child)) {
                int code = onMatchNode(nodes, start, end, i, child);
                if (code == 0) return false;
                if (code == 1) continue;
                if (code == 2) return true;
            }
        }
        return false;
    }

    /**
     * 重置匹配器
     */
    protected void onReset() {
    }

    /**
     * 判断是否为代码节点
     *
     * @param node 代码节点
     * @return true，代码节点；false，非代码节点
     */
    protected boolean isCodeNode(Node node) {
        return false;
    }

    /**
     * 匹配节点
     *
     * @param nodes 节点列表
     * @param start 开始
     * @param end   结束
     * @param index 下标
     * @param node  节点
     * @return 0，不匹配；1，匹配中；2，匹配结束
     */
    protected abstract int onMatchNode(List<Node> nodes, int start, int end, int index, Node node);
}
