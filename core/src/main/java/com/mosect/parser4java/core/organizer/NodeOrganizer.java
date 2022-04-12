package com.mosect.parser4java.core.organizer;

import com.mosect.parser4java.core.NodeList;

public interface NodeOrganizer extends NodeRegionParser {

    /**
     * 组织节点列表
     *
     * @param nodeList  节点列表
     * @param nodeStart 节点开始位置
     * @param nodeEnd   节点结束位置
     * @return 节点列表
     */
    NodeList organize(NodeList nodeList, int nodeStart, int nodeEnd);

    /**
     * 判断是否为兄弟区域
     *
     * @param region1 区间1
     * @param region2 区间2
     * @return true，兄弟区间；false，父子区间
     */
    boolean isBrotherRegion(NodeRegion region1, NodeRegion region2);
}
