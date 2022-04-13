package com.mosect.parser4java.core;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * 节点
 */
public interface Node extends Iterable<Node>, NodeList {

    @Override
    default int getNodeCount() {
        return getChildCount();
    }

    @Override
    default Node getNode(int index) {
        return getChild(index);
    }

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
     * 获取子节点
     *
     * @param index 下标
     * @return 子节点
     */
    Node getChild(int index);

    /**
     * 添加子节点
     *
     * @param index 下标
     * @param child 字节点
     */
    void addChild(int index, Node child);

    /**
     * 添加子节点
     *
     * @param child 字节点
     * @return true，添加成功；false，添加失败
     */
    boolean addChild(Node child);

    /**
     * 移除子节点
     *
     * @param child 子节点
     * @return true，移除成功；false，移除失败
     */
    boolean removeChild(Node child);

    /**
     * 移除子节点
     *
     * @param index 下标
     * @return 子节点
     */
    Node removeChild(int index);

    /**
     * 设置子节点
     *
     * @param index 下标
     * @param child 子节点
     * @return 返回旧的子节点
     */
    Node setChild(int index, Node child);

    /**
     * 清空所有子节点
     */
    void clearAllChild();

    /**
     * 添加子节点列表
     *
     * @param children 子节点列表
     */
    boolean addChildren(Collection<Node> children);

    /**
     * 添加子节点列表
     *
     * @param children 子节点列表
     */
    void addChildren(NodeList children);

    /**
     * 获取父节点
     *
     * @return 父节点
     */
    Node getParent();

    /**
     * 设置父节点
     *
     * @param parent 父节点
     */
    void setParent(Node parent);

    /**
     * 获取字符数量
     *
     * @return 字符数量
     */
    int getCharCount();

    /**
     * 将节点字符串追加到目标对象
     *
     * @param out 目标输出对象
     */
    void append(Appendable out) throws IOException;

    /**
     * 检查节点
     *
     * @param outErrors 输出的错误
     * @return true，检查通过；false，检查没通过，即出现错误
     */
    boolean check(List<ParseError> outErrors);
}
