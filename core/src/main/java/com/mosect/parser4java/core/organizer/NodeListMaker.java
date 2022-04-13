package com.mosect.parser4java.core.organizer;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.NodeList;
import com.mosect.parser4java.core.util.ArrayNodeList;

import java.util.ArrayList;
import java.util.List;

public class NodeListMaker {

    public NodeList make(NodeList src, List<? extends NodeRegion> regions) {
        List<Node> root = new ArrayList<>();
        NodeRegion region = null;
        int regionIndex = 0;
        int addedRegionIndex = -1;
        for (int i = 0; i < src.getNodeCount(); i++) {
            Node node = src.getNode(i);

            // 查找符合当前节点位置的区域
            for (int ri = regionIndex; ri < regions.size(); ri++) {
                NodeRegion r = regions.get(ri);
                if (i >= r.getStart()) {
                    regionIndex = ri;
                    region = r;
                } else {
                    break;
                }
            }

            if (null == region) {
                // 不存在符合区域，添加到根列表
                root.add(node);
            } else {
                if (addedRegionIndex != regionIndex) {
                    if (null == region.getParent()) {
                        root.add(region.getNode());
                    } else {
                        region.getParent().getNode().addChild(region.getNode());
                    }
                    addedRegionIndex = regionIndex;
                }

                // 存在区域
                if (i < region.getEnd()) {
                    // 符合区域
                    region.getNode().addChild(node);
                } else {
                    // 不符合区域
                    Node parentNode = findParentNode(region, i);
                    if (null == parentNode) {
                        root.add(node);
                    } else {
                        parentNode.addChild(node);
                    }
                }
            }
        }
        return new ArrayNodeList(root);
    }

    private Node findParentNode(NodeRegion region, int offset) {
        NodeRegion parent = region.getParent();
        if (null != parent) {
            if (offset >= parent.getStart() && offset < parent.getEnd()) {
                return parent.getNode();
            }
            return findParentNode(parent, offset);
        }
        return null;
    }
}
