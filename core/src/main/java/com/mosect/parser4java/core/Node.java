package com.mosect.parser4java.core;

import java.io.IOException;
import java.util.List;

/**
 * 节点
 */
public interface Node extends Iterable<Node> {

    /**
     * 节点类型
     *
     * @return 节点类型
     */
    String getType();

    /**
     * 判断是否为token
     *
     * @return token
     */
    boolean isToken();

    /**
     * 获取子节点数量
     *
     * @return 子节点数量
     */
    int getChildCount();

    /**
     * 获取子节点列表
     *
     * @return 子节点列表
     */
    List<Node> getChildren();

    /**
     * 将节点字符串追加到目标对象
     *
     * @param out 目标输出对象
     */
    void append(Appendable out) throws IOException;
}
