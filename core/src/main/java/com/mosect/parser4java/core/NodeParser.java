package com.mosect.parser4java.core;

/**
 * 节点解析器
 */
public interface NodeParser {

    /**
     * 获取解析器名称
     *
     * @return 解析器名称
     */
    String getName();

    /**
     * 解析节点
     *
     * @param parent 父解析器
     * @param source 节点源
     * @return true，可以解析成节点；false，不能解析成节点；此返回值不代表解析节点成功
     */
    boolean parse(NodeParser parent, NodeSource source);

    /**
     * 获取错误数量
     *
     * @return 错误数量
     */
    int getErrorCount();

    /**
     * 获取错误
     *
     * @param index 错误下标
     * @return 错误
     */
    ParseError getError(int index);

    /**
     * 获取解析的节点
     *
     * @return 解析的节点
     */
    Node getNode();

    /**
     * 获取节点源
     *
     * @return 节点源
     */
    NodeSource getSource();
}
