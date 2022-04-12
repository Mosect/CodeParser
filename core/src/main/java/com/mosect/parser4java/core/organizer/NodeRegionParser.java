package com.mosect.parser4java.core.organizer;

/**
 * 节点区域解析器
 */
public interface NodeRegionParser {

    /**
     * 解析器名称
     *
     * @return 解析器名称
     */
    String getName();

    /**
     * 优先级，越大优先级越高
     *
     * @return 优先级
     */
    int getPriority();

    /**
     * 匹配区域
     *
     * @param context   节点上下文
     * @param parent    父节点区域
     * @param nodeStart 节点开始位置
     * @param nodeIndex 当前需要处理的节点位置
     * @return 节点区域，返回null表示不匹配节点区域
     */
    NodeRegion matchRegion(NodeContext context, NodeRegion parent, int nodeStart, int nodeIndex);

    /**
     * 处理区域
     *
     * @param context   节点上下文
     * @param region    节点区域
     * @param nodeIndex 当前需要处理的节点位置
     * @param nodeEnd   节点结束位置
     * @return true，表示进行了有效处理；false，无法处理
     */
    boolean processRegion(NodeContext context, NodeRegion region, int nodeIndex, int nodeEnd);
}
