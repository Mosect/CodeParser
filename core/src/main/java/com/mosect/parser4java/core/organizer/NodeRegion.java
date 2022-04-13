package com.mosect.parser4java.core.organizer;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.NodeList;

/**
 * 节点区域
 */
public interface NodeRegion {

    /**
     * 获取父区域
     *
     * @return 父区域
     */
    NodeRegion getParent();

    /**
     * 获取区域节点
     *
     * @return 区域节点
     */
    Node getNode();

    /**
     * 获取区域开始位置
     *
     * @return 区域开始位置
     */
    int getStart();

    /**
     * 获取区域结束位置
     *
     * @return 区域结束位置
     */
    int getEnd();
}
