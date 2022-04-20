package com.mosect.parser4java.core.node;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.NodeList;
import com.mosect.parser4java.core.util.ArrayNodeList;

public abstract class NodeParser {

    public void organize(Node node) {
        if (node.getChildCount() > 0) {
            organize(node, new ArrayNodeList(64));
        }
    }

    public void organize(Node node, ArrayNodeList srcTemp) {
        srcTemp.getList().clear();
        for (int i = 0; i < node.getChildCount(); i++) {
            srcTemp.getList().add(node.getChild(i));
        }
        node.clearAllChild();
        parse(srcTemp, 0, srcTemp.getNodeCount(), node);
    }

    public Node parseRoot(NodeList src, int start, int end) {
        Node root = createRootNode();
        parse(src, start, end, root);
        return root;
    }

    public void parse(NodeList src, int start, int end, Node out) {
        int nodeStart = -1;
        UnclosedNodes unclosedNodes = new UnclosedNodes();
        NodeInfo nodeInfo = new NodeInfo();
        unclosedNodes.add(out);
        int offset = start;
        while (offset < end) {
            Node node = src.getNode(offset);
            if (nodeStart < 0) {
                // 未存在有效开始节点
                if (isIgnoreNode(node)) {
                    // 忽略节点
                    unclosedNodes.current().addChild(node);
                    ++offset;
                    continue;
                }
                nodeStart = offset;
            }

            // 存在有效的开始节点

            // 查找节点结束位置
            int curEnd = offset + 1;
            int nodeEnd = unclosedNodes.getCount() > 1 ? getNodeEnd(unclosedNodes, src, nodeStart, curEnd) : -1;
            if (nodeEnd >= 0) {
                // 节点结束
                for (int i = nodeStart; i < nodeEnd; i++) {
                    unclosedNodes.current().addChild(src.getNode(i));
                }
                // 关闭最后的节点
                Node newClosedNode = unclosedNodes.current();
                unclosedNodes.closeLast();
                // 关闭最后一个节点，可能会引起其他其他未关闭节点进行关闭，隐藏要执行此方法
                closeNodes(unclosedNodes, newClosedNode);
                offset = nodeEnd;
                nodeStart = -1;
            } else {
                // 节点未结束
                createNode(src, nodeStart, curEnd, nodeInfo);
                if (nodeInfo.isExists()) {
                    // 创建节点成功
                    // 添加源节点至新节点
                    for (int i = nodeInfo.getStart(); i < curEnd; i++) {
                        nodeInfo.getNode().addChild(src.getNode(i));
                    }
                    // 需要将不属于新节点的添加至上个未关闭节点
                    for (int i = nodeStart; i < nodeInfo.getStart(); i++) {
                        unclosedNodes.current().addChild(src.getNode(i));
                    }
                    boolean brother = unclosedNodes.getCount() > 1 &&
                            isBrotherWithUnclosedNode(unclosedNodes.current(), nodeInfo.getNode());
                    if (brother) {
                        // 兄弟节点
                        unclosedNodes.current().getParent().addChild(nodeInfo.getNode());
                        unclosedNodes.closeLast();
                    } else {
                        // 父子节点
                        unclosedNodes.current().addChild(nodeInfo.getNode());
                    }
                    if (nodeInfo.isClosed()) {
                        // 新节点已关闭
                        // 新节点关闭，可能会引起其他其他未关闭节点进行关闭，隐藏要执行此方法
                        Node newClosedNode = nodeInfo.getNode(); // 新的已关闭节点
                        closeNodes(unclosedNodes, newClosedNode);
                    } else {
                        // 新节点未关闭
                        unclosedNodes.add(nodeInfo.getNode());
                    }

                    nodeInfo.clear();
                    nodeStart = -1;
                }
                ++offset;
            }
        }
        if (nodeStart >= 0) {
            for (int i = nodeStart; i < end; i++) {
                out.addChild(src.getNode(i));
            }
        }
    }

    protected boolean isIgnoreNode(Node node) {
        return false;
    }

    protected void createNode(NodeList src, int start, int end, NodeInfo out) {
    }

    protected int getNodeEnd(UnclosedNodes unclosedNodes, NodeList src, int start, int end) {
        return -1;
    }

    protected boolean isBrotherWithUnclosedNode(Node unclosedNode, Node node) {
        return false;
    }

    protected boolean canCloseNode(Node unclosedNode, Node newClosedNode) {
        return false;
    }

    protected void closeNodes(UnclosedNodes unclosedNodes, Node newClosedNode) {
        while (unclosedNodes.getCount() > 0) {
            boolean close = canCloseNode(unclosedNodes.current(), newClosedNode);
            if (close) {
                newClosedNode = unclosedNodes.current();
                unclosedNodes.closeLast();
            } else {
                break;
            }
        }
    }

    protected abstract Node createRootNode();
}
